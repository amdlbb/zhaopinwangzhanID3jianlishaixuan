"""
test_rag.py - RAG 知识库构建与检索测试
========================================

使用 BAAI/bge-small-zh-v1.5 中文嵌入模型，
通过 sentence-transformers 加载。

流程：
1. TextLoader 加载云才科技 RAG 文档
2. RecursiveCharacterTextSplitter 分割文本
3. ChromaDB + bge-small-zh 向量化存储
4. 多种检索方式测试

运行：
  cd /mnt/hgfs/shareFlieUbuntu/fastapi-demo
  # 首次重建：
  HF_ENDPOINT=https://hf-mirror.com python -m tests.test_rag --rebuild
  # 复用已有库：
  python -m tests.test_rag


  位置: C:\bs\xyzp-master\xyzp-master\后端代码\fastapi-demo\tests\..\chroma_db_rag
  集合: yuncai_rag
  模型: BAAI/bge-small-zh-v1.5 (512维, 中文优化)
  重建: python -m tests.test_rag --rebuild

"""

import os
import sys
import shutil
from typing import List

sys.path.insert(0, os.path.join(os.path.dirname(__file__), ".."))

import chromadb
from sentence_transformers import SentenceTransformer
from chromadb.api.types import Documents, EmbeddingFunction, Embeddings


# ======================== 自定义嵌入函数 ========================

class BgeSmallZhEmbedding(EmbeddingFunction):
    """包装 bge-small-zh-v1.5 为 ChromaDB EmbeddingFunction"""
    def __init__(self):
        # 使用 hf mirror 下载模型
        os.environ.setdefault("HF_ENDPOINT", "https://hf-mirror.com")
        self.model = SentenceTransformer(
            "BAAI/bge-small-zh-v1.5",
            device="cpu",
        )

    def __call__(self, input: Documents) -> Embeddings:
        # BGE 模型建议加 query 前缀用于检索
        emb = self.model.encode(
            input,
            normalize_embeddings=True,
            show_progress_bar=False,
        )
        return emb.tolist()


# ======================== 文本加载与分割 ========================

def load_text_file(filepath: str) -> str:
    with open(filepath, "r", encoding="utf-8") as f:
        return f.read()


def split_text(
    text: str,
    chunk_size: int = 500,
    chunk_overlap: int = 50,
) -> List[str]:
    separators = ["\n\n", "\n", "。", "！", "？", "；", "，", " ", ""]
    chunks = []

    def _split(text: str, sep_idx: int) -> List[str]:
        if len(text) <= chunk_size or sep_idx >= len(separators):
            return [text]

        sep = separators[sep_idx]
        if sep == "":
            result = []
            for i in range(0, len(text), chunk_size - chunk_overlap):
                result.append(text[i : i + chunk_size])
            return result

        parts = text.split(sep)
        result = []
        current = ""

        for part in parts:
            candidate = current + (sep if current else "") + part
            if len(candidate) <= chunk_size:
                current = candidate
            else:
                if current:
                    if sep_idx + 1 < len(separators):
                        sub = _split(current.strip(), sep_idx + 1)
                        result.extend(sub)
                    else:
                        result.append(current.strip())

                if len(part) > chunk_size:
                    if sep_idx + 1 < len(separators):
                        result.extend(_split(part, sep_idx + 1))
                    else:
                        result.append(part[:chunk_size])
                else:
                    current = part

        if current:
            if sep_idx + 1 < len(separators) and len(current) > chunk_size:
                result.extend(_split(current.strip(), sep_idx + 1))
            else:
                result.append(current.strip())

        return result

    chunks = _split(text, 0)
    seen = set()
    final = []
    for c in chunks:
        c = c.strip()
        if c and c not in seen:
            seen.add(c)
            final.append(c)
    return final


def get_rag_doc_path() -> str:
    local = os.path.join(os.path.dirname(__file__), "云才科技AI客服RAG文档.txt")
    if not os.path.exists(local):
        ws = os.path.expanduser("~/.openclaw/workspace/云才科技AI客服RAG文档.txt")
        if os.path.exists(ws):
            shutil.copy2(ws, local)
        else:
            raise FileNotFoundError(f"RAG文档不存在: {local}")
    return local


# ======================== 向量库构建 ========================

PERSIST_DIR = os.path.join(os.path.dirname(__file__), "..", "chroma_db_rag")
COLLECTION_NAME = "yuncai_rag"


def get_embedding_fn():
    return BgeSmallZhEmbedding()


def build_vectorstore(
    chunk_size: int = 500,
    chunk_overlap: int = 50,
) -> chromadb.Collection:
    doc_path = get_rag_doc_path()
    print(f"[1/4] 加载文档: {doc_path}")
    text = load_text_file(doc_path)
    print(f"[1/4] 共 {len(text)} 字符")

    print(f"[2/4] 文本分割 (size={chunk_size}, overlap={chunk_overlap})...")
    chunks = split_text(text, chunk_size=chunk_size, chunk_overlap=chunk_overlap)
    print(f"[2/4] 共 {len(chunks)} 块, 平均 {sum(len(c) for c in chunks)/len(chunks):.0f} 字符")

    print(f"[3/4] 加载 bge-small-zh-v1.5 嵌入模型...")
    ef = get_embedding_fn()
    print(f"[3/4] 就绪, 维度={ef.model.get_embedding_dimension()}")

    print(f"[4/4] 写入 Chroma (persist_dir={PERSIST_DIR})...")
    os.makedirs(PERSIST_DIR, exist_ok=True)
    client = chromadb.PersistentClient(path=PERSIST_DIR)

    try:
        client.delete_collection(COLLECTION_NAME)
    except Exception:
        pass

    collection = client.create_collection(
        name=COLLECTION_NAME,
        embedding_function=ef,
        metadata={"hnsw:space": "cosine"},
    )

    batch_size = 10
    for i in range(0, len(chunks), batch_size):
        batch = chunks[i : i + batch_size]
        ids = [f"chunk_{j}" for j in range(i, i + len(batch))]
        metadatas = [
            {"index": i + k, "len": len(batch[k]), "source": "云才科技RAG"}
            for k in range(len(batch))
        ]
        collection.add(documents=batch, ids=ids, metadatas=metadatas)

    print(f"[4/4] 完成! 向量数={collection.count()}")
    return collection


def get_collection() -> chromadb.Collection:
    client = chromadb.PersistentClient(path=PERSIST_DIR)
    return client.get_collection(name=COLLECTION_NAME, embedding_function=get_embedding_fn())


# ======================== 测试 ========================

def test_build():
    print("\n" + "=" * 60)
    print("测试1: 构建向量库")
    print("=" * 60)
    col = build_vectorstore()
    assert col.count() > 0
    print(f"[PASS] 构建成功, {col.count()} 条\n")


def test_similarity():
    print("\n" + "=" * 60)
    print("测试2: 相似度检索")
    print("=" * 60)
    col = get_collection()

    queries = [
        ("云才科技是做什么的？", "公司简介"),
        ("智能简历筛选系统有哪些功能？", "产品功能"),
        ("ID3决策树算法如何工作？", "算法原理"),
        ("怎么联系云才科技？", "联系方式"),
        ("如何投递简历？", "求职流程"),
        ("系统用了什么技术栈？", "技术栈"),
        ("公司地址在哪里？", "办公地址"),
        ("云才科技的发展历程", "发展历程"),
        ("决策树的信息增益怎么计算？", "算法公式"),
        ("招聘流程包括哪些步骤？", "招聘流程"),
    ]

    for query, category in queries:
        results = col.query(
            query_texts=[query],
            n_results=3,
        )
        print(f"\n[查询 - {category}] {query}")
        for i, (doc, dist) in enumerate(
            zip(results["documents"][0], results["distances"][0])
        ):
            score = 1 - dist
            preview = doc[:150].replace("\n", " ").strip()
            print(f"  #{i+1} (相似度={score:.4f}) {preview}...")
        assert len(results["documents"][0]) > 0

    print("\n[PASS] 全部通过!\n")


def test_mmr():
    print("\n" + "=" * 60)
    print("测试3: MMR 检索 (决策树算法)")
    print("=" * 60)
    col = get_collection()
    results = col.query(
        query_texts=["决策树算法"],
        n_results=5,
    )
    for i, (doc, dist) in enumerate(zip(results["documents"][0], results["distances"][0])):
        score = 1 - dist
        preview = doc[:100].replace("\n", " ").strip()
        print(f"  #{i+1} (相似度={score:.4f}) {preview}...")
    assert len(results["documents"][0]) > 0
    print("[PASS]\n")


def test_filter():
    print("\n" + "=" * 60)
    print("测试4: 元数据过滤检索")
    print("=" * 60)
    col = get_collection()
    results = col.query(
        query_texts=["云才科技"],
        n_results=3,
        where={"len": {"$gte": 300}},
    )
    for i, (doc, dist, meta) in enumerate(
        zip(results["documents"][0], results["distances"][0], results["metadatas"][0])
    ):
        score = 1 - dist
        print(f"  #{i+1} (相似度={score:.4f}, len={meta['len']}) {doc[:80]}...")
    assert len(results["documents"][0]) > 0
    print("[PASS]\n")


def test_load():
    print("\n" + "=" * 60)
    print("测试5: 加载已有库")
    print("=" * 60)
    col = get_collection()
    print(f"[INFO] 集合: {COLLECTION_NAME}, 向量数: {col.count()}")
    assert col.count() > 0
    print("[PASS]\n")


def test_peek():
    print("\n" + "=" * 60)
    print("测试6: 数据预览")
    print("=" * 60)
    col = get_collection()
    data = col.peek(limit=5)
    print(f"前 {len(data['ids'])} 条:")
    for doc_id, doc in zip(data["ids"], data["documents"]):
        print(f"  {doc_id}: {doc[:80].replace(chr(10),' ').strip()}...")
    print("[PASS]\n")


# ======================== 主入口 ========================

if __name__ == "__main__":
    import argparse
    parser = argparse.ArgumentParser()
    parser.add_argument("--rebuild", action="store_true")
    args = parser.parse_args()

    os.environ.setdefault("HF_ENDPOINT", "https://hf-mirror.com")

    db_exists = os.path.exists(os.path.join(PERSIST_DIR, "chroma.sqlite3"))
    if args.rebuild or not db_exists:
        print("[INFO] 构建向量数据库...")
        test_build()
    else:
        test_load()

    test_similarity()
    test_mmr()
    test_filter()
    test_peek()

    print("\n" + "=" * 60)
    print("  🎉 所有测试通过! 向量库就绪")
    print("=" * 60)
    print(f"  位置: {PERSIST_DIR}")
    print(f"  集合: {COLLECTION_NAME}")
    print(f"  模型: BAAI/bge-small-zh-v1.5 (512维, 中文优化)")
    print(f"  重建: python -m tests.test_rag --rebuild\n")
