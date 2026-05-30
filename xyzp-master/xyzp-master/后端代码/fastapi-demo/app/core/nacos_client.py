from nacos import NacosClient 

NACOS_SERVER = "192.168.44.1:8848"
NAMESPACE = "public"
GROUP = "DEFAULT_GROUP"
SERVICE_NAME = "fastapi-service"

class NacosService:
  def __init__(self):
    self.client = NacosClient(NACOS_SERVER, namespace=NAMESPACE)

  def register(self, ip, port):
    self.client.add_naming_instance(SERVICE_NAME, ip=ip, port=port)

  def deregister(self, ip, port):
    self.client.remove_naming_instance(SERVICE_NAME, ip=ip, port=port)

  def get_config(self, data_id):
    return self.client.get_config(data_id=data_id, group=GROUP)