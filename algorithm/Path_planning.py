import numpy as np
import matplotlib.pyplot as plt

# 定义栅格地图
grid_map = np.array([
    [0,0,0,0,0,0,0,0,1,1,0,1,0,0,0,0,0,0,0,0],
    [0,0,1,1,1,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0],
    [0,0,1,0,0,1,1,0,0,0,1,1,0,0,1,0,0,1,1,0],
    [0,0,0,0,0,0,1,1,1,0,1,1,0,0,0,0,0,1,1,0],
    [0,0,1,1,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0],
    [0,0,1,1,0,0,0,0,0,0,0,1,1,1,0,1,1,1,1,0],
    [0,0,0,0,0,0,1,0,0,0,0,1,1,1,0,1,1,1,1,0],
    [0,0,0,0,1,0,1,1,1,1,0,1,1,1,0,1,1,1,1,0],
    [0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0],
    [0,0,0,0,1,1,0,1,0,0,1,1,1,1,0,0,0,0,0,0],
    [0,0,0,0,0,1,0,1,1,0,1,1,1,1,0,0,0,0,0,0],
    [0,1,1,1,0,0,0,0,1,0,1,1,1,1,0,0,0,0,0,0],
    [0,1,1,1,0,0,1,1,1,0,1,1,1,1,0,0,0,0,0,0],
    [0,1,1,1,0,0,1,1,1,0,0,0,0,1,0,1,0,0,0,0],
    [1,1,0,0,0,1,1,1,1,1,1,0,0,1,0,0,0,0,1,0],
    [0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0],
    [0,0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,1,0,0,0],
    [0,1,1,0,0,0,1,1,1,0,1,1,0,0,0,0,1,1,1,0],
    [0,1,1,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0],
    [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
])

# 将栅格地图转换为距离矩阵
distances = np.array(grid_map) * 1000


# 定义蚂蚁群算法函数
def ant_colony_optimization(grid_map,fire_data,start,end,distances, n_ants, n_iterations, alpha, beta, rho, Q):
    # 避免除以零错误，将距离矩阵中的零值替换为一个很大的数
    distances[distances == 0] = 1e6
    grid_map[fire_data[0]][fire_data[1]]=1
    # 初始化信息素
    pheromone = np.ones(distances.shape) / distances

    # 初始化最佳路径和最短距离
    best_path = None
    best_distance = float('inf')

    # 追踪迭代次数和最佳距离
    iteration_count = []
    best_distance_history = []

    # 迭代
    for iteration in range(n_iterations):
        # 初始化蚂蚁的路径集合
        ant_paths = []

        # 每只蚂蚁搜索路径
        for _ in range(n_ants):
            # 初始化蚂蚁的起始位置和已访问节点集合
            current_node = start # 初始位置
            visited = set([current_node])
            path = [current_node]

            # 搜索路径直到到达终点
            while current_node != end:  # 终点在
                # 计算下一个节点的概率分布，排除障碍物节点
                x, y = current_node
                neighbors = [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]
                unvisited = [(nx, ny) for nx, ny in neighbors if
                             (nx, ny) not in visited and 0 <= nx < grid_map.shape[0] and 0 <= ny < grid_map.shape[1] and
                             grid_map[nx][ny] != 1]
                if len(unvisited) == 0:
                    # 如果没有可选的节点，回溯到上一个节点
                    if len(path) == 1:
                        # 如果已经回溯到起点，则退出循环
                        break
                    else:
                        # 回溯到上一个节点
                        path.pop()
                        current_node = path[-1]
                        continue  # 继续下一次循环
                else:
                    probabilities = np.array(
                        [pheromone[nx][ny] ** alpha * (1 / distances[nx][ny]) ** beta for nx, ny in unvisited])
                    if np.sum(probabilities) == 0:
                        # 如果所有相邻节点的概率之和为0，则回溯到上一个节点
                        if len(path) == 1:
                            # 如果已经回溯到起点，则退出循环
                            break
                        else:
                            # 回溯到上一个节点
                            path.pop()
                            current_node = path[-1]
                            continue  # 继续下一次循环

                    probabilities = probabilities / np.sum(probabilities)

                    # 根据概率选择下一个节点
                    next_node = unvisited[np.random.choice(len(unvisited), p=probabilities)]

                    # 更新当前节点、已访问的节点集合和路径
                    current_node = next_node
                    visited.add(current_node)
                    path.append(current_node)

            # 添加路径到路径集合中
            ant_paths.append(path)

        # 更新全局最佳路径
        for path in ant_paths:
            total_distance = sum(distances[path[i][0], path[i][1]] for i in range(len(path) - 1))
            if total_distance < best_distance:
                best_path = path
                best_distance = total_distance

        # 记录最佳距离历史
        iteration_count.append(iteration)
        best_distance_history.append(best_distance)

        # 更新信息素
        pheromone *= (1 - rho)
        for path in ant_paths:
            for i in range(len(path) - 1):
                x, y = path[i]
                nx, ny = path[i + 1]
                pheromone[nx][ny] += Q / distances[x][y]

    return best_path, best_distance, iteration_count, best_distance_history


# 运行蚂蚁群算法
best_path1, best_distance1, iteration_count1, best_distance_history1 = ant_colony_optimization(grid_map, (5,4),(8,10),(grid_map.shape[0] - 1, grid_map.shape[1] - 1),distances,n_ants=50, n_iterations=100,alpha=1, beta=7, rho=0.5,Q=1)
best_path2, best_distance2, iteration_count2, best_distance_history2 = ant_colony_optimization(grid_map, (5,4),(8,10),(grid_map.shape[0] - 1, 0),distances,n_ants=50, n_iterations=100,alpha=1, beta=7, rho=0.5,Q=1)
best_path3, best_distance3, iteration_count3, best_distance_history3 = ant_colony_optimization(grid_map, (5,4),(8,10),(0, grid_map.shape[1] - 1),distances,n_ants=50, n_iterations=100,alpha=1, beta=7, rho=0.5,Q=1)
best_path4, best_distance4, iteration_count4, best_distance_history4 = ant_colony_optimization(grid_map, (5,4),(8,10),(0, 0),distances,n_ants=50, n_iterations=100,alpha=1, beta=7, rho=0.5,Q=1)

best_d = [best_distance1,best_distance2,best_distance3,best_distance4]
best_p = [best_path1,best_path2,best_path3,best_path4]
best_d_h = [best_distance_history1,best_distance_history2,best_distance_history3,best_distance_history4]
iteration_c = [iteration_count1,iteration_count2,iteration_count3,iteration_count4]

best_path = best_path1
best_distance = best_distance1
best_distance_history= best_distance_history1
iteration_count = iteration_count1

for i in range(1,4) :
    if best_d[i]<best_distance:
        best_distance=best_d[i]
        best_path=best_p[i]
        best_distance_history = best_d_h[i]
        iteration_count = iteration_c[i]

# 打印最佳路径和最短距离
print("Best Path:", best_path)
print("Best Distance:", best_distance)

# 绘制最佳路径
plt.imshow(grid_map, cmap='Greys', origin='lower')
plt.scatter([5], [5], color='red', s=100, marker='*')
plt.text(22, 19.6, 'Entrance 4', ha='center', va='center', color='blue')
plt.text(-2.5, 19.6, 'Entrance 3', ha='center', va='center', color='blue')
plt.text(22, -0.5, 'Entrance 2', ha='center', va='center', color='blue')
plt.text(-2.5, -0.5, 'Entrance 1', ha='center', va='center', color='blue')
plt.plot([j for _, j in best_path1], [i for i, _ in best_path1], marker='o', color='red')
plt.plot([j for _, j in best_path2], [i for i, _ in best_path2], marker='o', color='blue')
plt.plot([j for _, j in best_path3], [i for i, _ in best_path3], marker='o', color='yellow')
plt.plot([j for _, j in best_path4], [i for i, _ in best_path4], marker='o', color='green')
plt.title('Four Best Path')
plt.xlabel('X')
plt.ylabel('Y')
plt.grid(True)
plt.show()


# 绘制最佳路径
plt.imshow(grid_map, cmap='Greys', origin='lower')
plt.scatter([5], [5], color='red', s=100, marker='*')
plt.text(22, 19.6, 'Entrance 4', ha='center', va='center', color='blue')
plt.text(-2.5, 19.6, 'Entrance 3', ha='center', va='center', color='blue')
plt.text(22, -0.5, 'Entrance 2', ha='center', va='center', color='blue')
plt.text(-2.5, -0.5, 'Entrance 1', ha='center', va='center', color='blue')
plt.plot([j for _, j in best_path], [i for i, _ in best_path], marker='o', color='blue')
plt.title('Best Path')
plt.xlabel('X')
plt.ylabel('Y')
plt.grid(True)
plt.show()

# 绘制迭代次数与最佳距离的图表
plt.plot(iteration_count, best_distance_history)
plt.title('Iteration vs. Best Distance')
plt.xlabel('Iteration')
plt.ylabel('Best Distance')
plt.grid(True)
plt.show()