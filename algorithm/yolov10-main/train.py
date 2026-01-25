from ultralytics import YOLOv10

# 数据集配置文件
data_yaml_path = 'data.yaml'
# 预训练模型
pre_model_name = 'yolov10x.yaml'

if __name__ == '__main__':
    # 加载预训练模型
    model = YOLOv10(pre_model_name)
    # 训练生成的文件保存路径名
    savename = 'train_yolov10x'
    # 训练模型
    results = model.train(data=data_yaml_path,
                          epochs=50,
                          name=savename)