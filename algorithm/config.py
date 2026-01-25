# name of config can only UpperCase
class Config:
    BACKEND_URL = "http://101.43.254.115:10115"#java后端
    TOKEN = None
    MONITOR_ID = -1
    LATITUDE = 0
    LONGITUDE = 0
    TYPE_LIST = []
    AREA_LIST = []


class DevConfig(Config):
    TOKEN = None
    pass


class ProdConfig(Config):
    TOKEN = None


config = {
    "dev": DevConfig,
    "prod": ProdConfig
}

