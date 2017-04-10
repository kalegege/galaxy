/*
*	edit by shy
*	edit time 2015-09-10 00:00
*	modify by shy
*	modify time 2015-10-21 09:37
*	
*	彩虹影院数据页（杭州）
*	正片：http://hd2.hzdtv.tv/a?f=mov_hot_69_5&profile=HZ-NEW-SCENARIO
*	正片片花：http://hd2.hzdtv.tv/a?f=mov_hot_69_5_1&profile=HZ-NEW-SCENARIO
*	购票片花：http://hd2.hzdtv.tv/a?f=mov_hot_69_5_2&profile=HZ-NEW-SCENARIO
*	
*	彩虹影院数据页（太原）
*	正片：http://taiyuan-utc.wasu.cn/a?f=mov_hot_69_5&profile=TAIYUAN22SCENARIO
*	正片片花：http://taiyuan-utc.wasu.cn/a?f=mov_hot_69_5_1&profile=TAIYUAN22SCENARIO
*	购票片花：http://taiyuan-utc.wasu.cn/a?f=mov_hot_69_5_2&profile=TAIYUAN22SCENARIO
*/
//var countdownTimeDefault = 120;  //二维码弹框倒计时 单位：秒

//var ajaxIp = "http://125.210.142.73/galaxy"; //测试数据接口访问IP地址
var ajaxIp = "http://125.210.135.171/galaxy"; //正式数据接口访问IP地址
//var ajaxIp = "http://video.wasu.cn/galaxy"; //正式数据接口访问IP地址
var region_code = "taiyuan";


function createJavaScript(url){
   var myScript= document.createElement("script");
   myScript.type = "text/javascript";
   myScript.src=url;
   document.body.appendChild(myScript);
}
/*
*	价钱转换 单位分转为元 start
*/
function isPositiveNum(s){//是否为正整数 
	var re = /^[0-9]*[1-9][0-9]*$/ ; 
	return re.test(s) 
} 
function priceCentsToYuan(cents) {
	var yuan = parseInt(cents)/100;
	if(isPositiveNum(yuan)) yuan = yuan + ".0";
	return yuan
}
/*
*	价钱转换 单位分转为元 end
*/

/*
*	中文字符编码加密解密 start
*/
function native2ascii(strNative) { 
     var output = ""; 
     for (var i=0; i<strNative.length; i++) { 
         var c = strNative.charAt(i); 
         var cc = strNative.charCodeAt(i); 
         if (cc > 0xff) 
           output += "\\u" + toHex(cc >>  8 ) + toHex(cc & 0xff); 
         else 
           output += c; 
     } 
     return output; 
} 
var hexChars = "0123456789ABCDEF"; 
function toHex(n) { 
     var nH = (n >> 4) & 0x0f; 
     var nL = n & 0x0f; 
     return hexChars.charAt(nH) + hexChars.charAt(nL); 
}
function ascii2native(strAscii) { 
     var output = ""; 
     var posFrom = 0; 
     var posTo = strAscii.indexOf("\\u", posFrom); 
     while (posTo >= 0) { 
         output += strAscii.substring(posFrom, posTo); 
         output += toChar(strAscii.substr(posTo, 6)); 
         posFrom = posTo + 6; 
         posTo = strAscii.indexOf("\\u", posFrom); 
     } 
     output += strAscii.substr(posFrom); 
     return output; 
} 

function toChar(str) { 
     if (str.substr(0, 2) != "\\u") return str; 

     var code = 0; 
     for (var i=2; i<str.length; i++) { 
         var cc = str.charCodeAt(i); 
         if (cc >= 0x30 && cc <= 0x39) 
             cc = cc - 0x30; 
         else if (cc >= 0x41 && cc <= 0x5A) 
             cc = cc - 0x41 + 10; 
         else if (cc >= 0x61 && cc <= 0x7A) 
             cc = cc - 0x61 + 10; 

         code <<= 4; 
         code += cc; 
     } 

     if (code < 0xff) return str; 

     return String.fromCharCode(code); 
} 
/*
*	中文字符编码加密解密 end
*/
