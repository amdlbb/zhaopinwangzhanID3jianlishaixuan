# TODO: 完成向量检索器的编写

import os

#向量库构建示例
PERSIST_DIR = os.path.join(os.path.dirname(__file__), "..", "chroma_db_rag")
embeddings = HuggingFaceEmbeddings(model_name="BAAI/bge-small-zh-v1.5") # type: ignore



class Vectorstore(object):
    def __init__(self):
        self.vectorstore = Chroma( # type: ignore
          persist_directory=PERSIST_DIR, # type: ignore
          embedding_function=embeddings
        )

    def get_retriever(self):
        return self.vectorstore.as_retriever(search_kwargs={"k",3})