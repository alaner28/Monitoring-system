from .monitorController import monitor
DEFAULT_BLUEPRINT = [
    # (蓝图, url前缀)
    (monitor, '/api/v1/monitor-device'),
]


def config_blueprint(app):
    for blueprint, prefix in DEFAULT_BLUEPRINT:
        app.register_blueprint(blueprint, url_prefix=prefix)
