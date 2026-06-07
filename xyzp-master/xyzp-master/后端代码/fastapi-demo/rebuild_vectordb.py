import os
os.environ.pop('http_proxy', None)
os.environ.pop('https_proxy', None)
os.environ.pop('HTTP_PROXY', None)
os.environ.pop('HTTPS_PROXY', None)
os.environ.pop('all_proxy', None)
os.environ.pop('ALL_PROXY', None)

import chromadb
from sentence_transformers import SentenceTransformer
from chromadb.api.types import Documents, EmbeddingFunction, Embeddings

# Use local cached model
MODEL_PATH = os.path.expanduser('~/.cache/huggingface/hub/models--BAAI--bge-small-zh-v1.5/snapshots')
for name in sorted(os.listdir(MODEL_PATH), reverse=True):
    candidate = os.path.join(MODEL_PATH, name)
    if os.path.isdir(candidate):
        MODEL_PATH = candidate
        break

print(f'Model path: {MODEL_PATH}')

class BgeEmbed(EmbeddingFunction):
    def __init__(self):
        self.model = SentenceTransformer(MODEL_PATH, device='cpu')
    def __call__(self, input: Documents) -> Embeddings:
        return self.model.encode(input, normalize_embeddings=True).tolist()

PERSIST_DIR = 'chroma_db_rag'
text = open('tests/云才科技AI客服RAG文档.txt', encoding='utf-8').read()

chunks = []
for para in text.split('\n\n'):
    para = para.strip()
    if para and len(para) > 20:
        if len(para) > 500:
            for i in range(0, len(para), 450):
                chunks.append(para[i:i+500])
        else:
            chunks.append(para)

print(f'Chunks: {len(chunks)}')
os.makedirs(PERSIST_DIR, exist_ok=True)
client = chromadb.PersistentClient(path=PERSIST_DIR)
try: client.delete_collection('yuncai_rag')
except: pass
ef = BgeEmbed()
col = client.create_collection(name='yuncai_rag', embedding_function=ef, metadata={'hnsw:space': 'cosine'})
for i in range(0, len(chunks), 10):
    batch = chunks[i:i+10]
    ids = [f'chunk_{j}' for j in range(i, i+len(batch))]
    col.add(documents=batch, ids=ids)
print(f'Built: {col.count()} vectors')
