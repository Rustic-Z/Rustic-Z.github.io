/**
 * 日期格式转换,将改方法做为插件添加至JQuery中
 */
Date.prototype.format = function(format) {
    var o = {
        "M+" : this.getMonth() + 1, // 月
        "d+" : this.getDate(), // 天
        "h+" : this.getHours(), // 时
        "m+" : this.getMinutes(), // 分
        "s+" : this.getSeconds(), // 秒
        "q+" : Math.floor((this.getMonth() + 3) / 3), // 刻
        "S" : this.getMilliseconds() //毫秒
    }
    if (/(y+)/.test(format)) {
    	format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
    	if (new RegExp("(" + k + ")").test(format)) {
    		format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    	}
    }
    return format;
}

/**
 * 日期格式转换,传入参数后转换
 */
function format1(date, format) {
    var o = {
        "M+" : date.getMonth() + 1, // 月
        "d+" : date.getDate(), // 天
        "h+" : date.getHours(), // 时
        "m+" : date.getMinutes(), // 分
        "s+" : date.getSeconds(), // 秒
        "q+" : Math.floor((date.getMonth() + 3) / 3), // 刻
        "S" : date.getMilliseconds() //毫秒
    }
    if (/(y+)/.test(format)) {
    	format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
    	if (new RegExp("(" + k + ")").test(format)) {
    		format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    	}
    }
    return format;
}
