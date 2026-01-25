<template>
  <div class="panel">
        <div class="chart" ref="linechart" id="demoDiv">
            <div class="realpeople">
                <div class="content">
                    <div>实时</div>
                    <div>人流</div>
                    
                </div>
                <div class="digits">
                    <div class="digit">{{ parseInt(realpeople/1000) }}</div>
                    <div class="digit">{{ parseInt(realpeople/100%10) }}</div>
                    <div class="digit">{{ parseInt(realpeople/10%10) }}</div>
                    <div class="digit">{{ parseInt(realpeople%10) }}</div>
                </div>
            </div>
            <div class="daypeople">
                <div class="content">
                    <div>当日</div>
                    <div>人流</div>
                    
                </div>
                <div class="digits">
                    <div class="digit">{{ parseInt(daypeople/1000) }}</div>
                    <div class="digit">{{ parseInt(daypeople/100%10) }}</div>
                    <div class="digit">{{ parseInt(daypeople/10%10) }}</div>
                    <div class="digit">{{ parseInt(daypeople%10) }}</div>
                </div>
            </div>
        </div>
        <div class="panel-footer"></div>
    </div>
</template>

<script>
export default {
    name:'people-1',
    data() {
        return {
            realpeople:2,
            daypeople:134
        }
    },
}
</script>

<style lang="less" scoped>
    #demoDiv {
        width: 100%;
        height: 100%;
        color: white;

        .realpeople, .daypeople {
            display: flex;
            height: 50%;
            align-items: center;

            .digits {
                width: 24rem;
                height: 7rem;
                font-size: 2rem;
                font-weight: 600;
                margin-left: 2.2rem;
                display: flex;
                align-items: center;

                .digit {
                    width: 4rem;
                    height: 6rem;
                    background-color: #07253F;
                    margin-left: 0.5rem;
                    margin-right: 1rem;
                    border: 1px solid white;

                    /* 裁剪四个角 */
                    border-radius: 10px;  /* 可以根据需要调整 */
                    font-size: 4.5rem;
                    line-height: 6rem;
                    text-shadow: 
                        0.2rem 0.4rem 1rem rgba(255, 255, 255, .4), /* 右下角阴影 */
                        -0.2rem -0.4rem 1rem rgba(255, 255, 255, .4), /* 左上角阴影 */
                        0.2rem -0.4rem 1rem rgba(255, 255, 255, .4), /* 右上角阴影 */
                        -0.2rem 0.4rem 1rem rgba(255, 255, 255, .4); /* 左下角阴影 */

                }
            }
        }

        .realpeople .content, .daypeople .content {
            width: 6.5rem;
            // height: 5.5rem;
            font-size: 1.8rem;
            font-weight: 600;
            // padding-right: 0.3rem;
            // margin-left: 0.5rem;
        }
    }
</style>
