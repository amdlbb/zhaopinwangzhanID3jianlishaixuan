"""
vector_store.py — ChromaDB 向量检索器
=======================================

【功能】
  加载持久化 ChromaDB 向量库，提供检索接口给 rag_service 使用。
  使用 BAAI/bge-small-zh-v1.5 中文嵌入模型（512维）。
  模型从本地缓存路径加载。

【用法】
  vs = VectorStore()
  retriever = vs.get_retriever()
  docs = vs.similarity_search("问题")
"""

import os
from langchain_chroma import Chroma
from langchain_huggingface import HuggingFaceEmbeddings

# 向量库持久化路径
PERSIST_DIR = os.path.join(os.path.dirname(__file__), "..", "..", "chroma_db_rag")
COLLECTION_NAME = "yuncai_rag"

# 本地模型路径（从 HF 缓存中查找）
_HF_CACHE = os.path.expanduser("~/.cache/huggingface/hub")
_MODEL_PATH = None

def _find_model_path():
    """查找本地缓存的 bge-small-zh 模型路径"""
    global _MODEL_PATH
    if _MODEL_PATH is not None:
        return _MODEL_PATH

    model_dir = os.path.join(_HF_CACHE, "models--BAAI--bge-small-zh-v1.5")
    if os.path.isdir(model_dir):
        snapshots = os.path.join(model_dir, "snapshots")
        if os.path.isdir(snapshots):
            for name in os.listdir(snapshots):
                candidate = os.path.join(snapshots, name)
                if os.path.isdir(candidate):
                    _MODEL_PATH = candidate
                    return _MODEL_PATH
    return None

# 全局嵌入模型实例（延迟初始化）
_embeddings = None


def _get_embeddings():
    """延迟加载嵌入模型"""
    global _embeddings
    if _embeddings is None:
        model_path = _find_model_path()
        if not model_path:
            model_path = "BAAI/bge-small-zh-v1.5"

        _embeddings = HuggingFaceEmbeddings(
            model_name=model_path,
            model_kwargs={"device": "cpu"},
            encode_kwargs={"normalize_embeddings": True},
        )
    return _embeddings


class VectorStore:
    """ChromaDB 向量检索器封装"""

    def __init__(self):
        embeddings = _get_embeddings()
        self._vectorstore = Chroma(
            persist_directory=PERSIST_DIR,
            embedding_function=embeddings,
            collection_name=COLLECTION_NAME,
        )

    def get_retriever(self, k: int = 3):
        """获取 LangChain retriever"""
        return self._vectorstore.as_retriever(search_kwargs={"k": k})

    def similarity_search(self, query: str, k: int = 3):
        """检索最相关的 k 个文档片段，返回 (Document, score) 列表"""
        return self._vectorstore.similarity_search_with_relevance_scores(query, k=k)
