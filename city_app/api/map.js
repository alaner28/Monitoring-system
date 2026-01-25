export function mymap(ak) {
  return new Promise(function(resolve, reject) {
    window.init = function() {
      resolve(mymap)
    }
    var script = document.createElement('script')
    script.type = 'text/javascript'    
    script.src = `http://api.map.baidu.com/api?v=1.0&type=webgl&ak=${ak}&mcode=35:81:1C:D3:2A:63:FB:6B:6E:1C:D4:F7:81:DF:A5:1B:89:57:83:91;uni.UNI342DC80&callback=init`	
    script.onerror = reject
    document.head.appendChild(script)
  })
}