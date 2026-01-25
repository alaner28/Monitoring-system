import xml.etree.ElementTree as ET
import os
from os import getcwd
import glob

# 1. 设置文件夹路径
image_set = r"抽烟xml"  # 需要转换的文件夹名称
imageset2 = r"抽烟txt"  # 保存txt的文件夹

# 2. 设置类别
classes = ['smoking']  # 标注时的标签

# 3. 设置工作目录（当前路径或指定路径）
data_dir = r"D:\桌面\yolov10-main\dataset"  # XML文件所在目录的父目录

# 定义转换函数


def convert(size, box):
    dw = 1. / size[0]
    dh = 1. / size[1]
    x = (box[0] + box[1]) / 2.0
    y = (box[2] + box[3]) / 2.0
    w = box[1] - box[0]
    h = box[3] - box[2]
    x = x * dw
    w = w * dw
    y = y * dh
    h = h * dh
    return (x, y, w, h)


def convert_annotation(data_dir, imageset1, imageset2, image_id):
    in_file_path = os.path.join(data_dir, imageset1, '%s.xml' % image_id)
    out_file_path = os.path.join(data_dir, imageset2, '%s.txt' % image_id)

    in_file = open(in_file_path, encoding='utf-8')  # 读取xml
    out_file = open(out_file_path, 'w', encoding='utf-8')  # 保存txt

    # ...（此处代码不变）...
    tree = ET.parse(in_file)
    root = tree.getroot()
    size = root.find('size')
    w = int(size.find('width').text)
    h = int(size.find('height').text)
    for obj in root.iter('object'):
        difficult = obj.find('difficult').text
        cls = obj.find('name').text
        if cls not in classes or int(difficult) == 1:
            continue
        cls_id = classes.index(cls)  # 获取类别索引
        xmlbox = obj.find('bndbox')
        b = (float(xmlbox.find('xmin').text), float(xmlbox.find('xmax').text), float(xmlbox.find('ymin').text),
             float(xmlbox.find('ymax').text))
        bb = convert((w, h), b)
        out_file.write(str(cls_id) + " " +
                       " ".join([str('%.6f' % a) for a in bb]) + '\n')


# 获取所有XML文件
image_ids = []
for x in glob.glob(os.path.join(data_dir, image_set, '*.xml')):
    image_ids.append(os.path.basename(x)[:-4])
print('\n%s数量:' % image_set, len(image_ids))  # 确认数量

# 转换XML文件
i = 0
for image_id in image_ids:
    i += 1
    convert_annotation(data_dir, image_set, imageset2, image_id)
    print("%s 数据:%s/%s文件完成！" % (image_set, i, len(image_ids)))

print("Done!!!")
