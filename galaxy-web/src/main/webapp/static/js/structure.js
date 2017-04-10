///////////全局变量\\\\\\\\\\\\\\\\\\\\\\
var logPath="http://125.210.228.106/logging/index.jsp";
var LogReferPage=encodeURIComponent(window.location.href);
var ConfigRootPath = "/template_images/"+getCookie("cityCode");
var KEY_PAGEUP = 372;
var KEY_PAGEDOWN = 373;
var KEY_LEFT = 37;
var KEY_UP = 38;
var KEY_RIGHT = 39;
var KEY_DOWN = 40;
var KEY_BACK = 45;
var KEY_OK = 13;
var KEY_QUIT = 339;
//var KEY_0 = 48;
//var KEY_1 = 49;
//var KEY_2 = 50;
//var KEY_3 = 51;
//var KEY_4 = 52;
//var KEY_5 = 53;
//var KEY_6 = 54;
//var KEY_7 = 55;
//var KEY_8 = 56;
//var KEY_9 = 57;
//var KEY_MENU = 32;//空格
//var KEY_HELP = 18;//F1
///////////////////////
function CommonEvent() {  /*按键框架*/
  var process;
  this.setProcess = function(obj) {
    process = obj
  };
  this.getProcess = function() {
    return process
  };
  this.processEvent = function(event) {
    if (event == null) {
      return
    }
    var key_code = event.which ? event.which: event.keyCode;
    switch (key_code) {
      case 1:  //ipannel 
      case 65362: //天柏
      case KEY_UP:
        process.keyUP();
        return 0;
        break;
      case 2:  //ipannel 
      case 65364: //天柏
      case KEY_DOWN:
        process.keyDown();
        return 0;
        break;
      case 3: //ipannel 
      case 65361: //天柏
      case KEY_LEFT:
        process.keyLeft();
        return 0;
        break;
      case 4: //ipannel
      case 65363: //天柏
      case KEY_RIGHT:
        process.keyRight();
        return 0;
        break;
      case KEY_OK:
      case 65293: //天柏
        process.doSelected();
        return 0;
        break;
      case 18:  
      case 340: //ipannel 返回
      case 65367: //天柏
      case KEY_BACK:
        process.comeBack();
        return 0;
        break;
            case KEY_QUIT:
        process.keyQuit();
                return 0;
        break;
      case KEY_PAGEUP:
        process.pageUp();
        return 0;
        break;
      case KEY_PAGEDOWN:
        process.pageDown();
        return 0;
        break;
      default:
        return 1;
        break;
    }
  }
}
function keyBinds(_Obj){    /*实例按钮*/
  document.onkeydown = function(evt){
    var A = new CommonEvent();
    A.setProcess(_Obj);
    var returnValue=A.processEvent(evt);
    return returnValue;
  };  
}

/*_pos表示页面区域内各标签的顺序位子，_objStr表示区域标签的父节点ID， _navList表示JS生成的存数据的数组（一般用于异步加载），_lineNum表示区域内按规律每行或列排的资产个数（一般用于列表页），_strMap表示用于传入区域移动的OBJ（一般可不写，在后面重写）*/
function BaseAreaDate(_pos,_objStr,_clssName,_navList,_lineNum,_strMap){
  this.objId = _objStr;
  this.pageNum = 1;
  this.FocusClass = _clssName;    /*表示该区域获得焦点时样式名称*/
  /*获取该区域内所有标签的对象集*/
  var labelList = ele(_objStr).childNodes;      /*通过父节点的ID，获得其下的所有子节点*/
  for(var i=0;i<labelList.length;i++){
    if(labelList[i].nodeType!=1){
      ele(_objStr).removeChild(labelList[i]);     /*删除其下的所有不是标签的子节点*/
    }
  }
  this.labelBtn = labelList;        /*该区域内所有标签的集合*/
  this.maxPos = this.labelBtn.length; /*该区域内标签的总个数*/
  this.pos = _pos;          /*该区域内获得焦点标签的索引值*/  
  this.id = this.labelBtn[this.pos-1].id;   /*该区域内获得焦点标签的ID*/

/*以下是内部属性*/
  /*设定资产的数组，当_navList参数未传入时，资产数组等于该区域内所有标签的集合，数组长度为该区域内标签的总个数*/
  if(typeof(_navList)=="undefined" || _navList == null){
    this.navList = this.labelBtn;     /*该区域所有资产的数组*/
    this.listlength = this.maxPos;      /*该区域所有资产的数组的长度*/
  }
  /*设定资产的数组，当_navList参数传入时，资产数组等于_navList，数组长度为_navList的长度*/
  if(typeof(_navList)!="undefined" && _navList != null){
    this.navList =  _navList;       /*该区域所有资产的数组*/
    this.listlength = _navList.length;    /*该区域所有资产的数组的长度*/
  }
  this.listIndex = this.pos + ((this.pageNum-1)*this.maxPos);   /*表示获得焦点的资产在数组中的索引*/
  /*区域翻页边缘值：linePos，第一列/行最大索引值；lastlinePos，倒数第二列/行最大索引值*/
  this.linePos = 1;
  if(typeof(_lineNum)!="undefined" && _lineNum != null && _lineNum <= this.maxPos){
    this.linePos = _lineNum;              
  }
  this.lastlinePos = this.maxPos-this.linePos;
  /*区域页数：pageNum，区域中当前页码；totlePageNum，区域总页数;endPagePosNum，最后一页总资产个数*/
  this.totlePageNum = Math.ceil(this.listlength/this.maxPos);   
  this.endPagePosNum = this.listlength-((this.totlePageNum-1)*this.maxPos);
  /*指定区域属性：areaMapLeft，左边区域实例对象；areaMapRight，右边区域实例对象；areaMapTop，上部区域实例对象；areaMapBottom，底部区域实例对象*/
  if(typeof(_strMap)!="undefined"){
    eval("var _areaMap = " + _strMap);
    if (_areaMap != undefined && _areaMap!= null && _areaMap.length > 0) {
      this.areaMapLeft = _areaMap[0].Left;
      this.areaMapRight = _areaMap[0].Right;
      this.areaMapTop = _areaMap[0].Top;
      this.areaMapBottom = _areaMap[0].Bottom;
    } 
  }

/*以下是事件属性*/
  this.onFocus = function(){          /*获取焦点事件*/
    addClass(this.id,this.FocusClass);
  };
  this.lostFocus = function(){          /*丢失焦点事件*/
    removeClass(this.id,this.FocusClass);
  }
  this.navPosChange = function(_inc){     /*焦点区域内移动事件*/
    var maxPos2 = this.maxPos;
    if(this.pageNum == this.totlePageNum){
      maxPos2 = this.endPagePosNum;
    }
    if(this.pos + _inc < 1 && this.pageNum > 1){
      this.hidUp();
      this.lostFocus();
      this.pos = this.pos + _inc + this.maxPos;
      this.id = this.labelBtn[this.pos-1].id;
      this.pageUp();
      this.onFocus();
      this.showDown();
    }
    else if(this.pos + _inc <=maxPos2 && this.pos + _inc > 0){
      if(this.pos <= this.linePos){
        this.hidUp();
      }
      if(this.pos > this.lastlinePos){
        this.hidDown();
      }
      this.lostFocus();   
      this.pos = this.pos + _inc;
      this.id = this.labelBtn[this.pos-1].id;
      this.onFocus();
      if(this.pos <= this.linePos){
        this.showUp();
      }
      if(this.pos > this.lastlinePos){
        this.showDown();
      }
    }
    else if(this.pos + _inc > this.maxPos && this.pageNum != this.totlePageNum){
      this.hidDown();
      this.lostFocus();
      this.pos = this.pos + _inc - this.maxPos;
      this.id = this.labelBtn[this.pos-1].id;
      this.pageDown();
      if(this.pos > this.endPagePosNum && this.pageNum==this.totlePageNum){
        this.pos =  this.endPagePosNum;
        this.id = this.labelBtn[this.pos-1].id;   
      }
      this.onFocus();     
      this.showUp();
    }
  };
  this.navAreaChange = function(_obj){  /*跨区域移动事件*/
    this.lostFocus();
    this.pos = _pos;
    this.id = this.labelBtn[this.pos-1].id;
    _obj.onFocus();
    keyBinds(_obj);
  }
}


BaseAreaDate.prototype = {
  keyUP:function(){
    if(this.pos<= this.linePos && this.pageNum ==1 && typeof(this.areaMapTop)!="undefined"){
      this.navAreaChange(this.areaMapTop);
    }
    else{
      this.navPosChange(-this.linePos);
    }
  },
  keyDown:function(){     /*区域内向下键值事件*/
    if(this.pos > this.endPagePosNum - this.linePos && this.pageNum == this.totlePageNum && typeof(this.areaMapBottom)!="undefined"){
      this.navAreaChange(this.areaMapBottom);
    }   
    else{
      this.navPosChange(this.linePos);
    }
  },
  keyRight:function(){    /*区域内向右键值事件*/
    if(this.pos%this.linePos ==0 && typeof(this.areaMapRight)!="undefined" && this.linePos!=1){
      this.navAreaChange(this.areaMapRight);
    }
    else if(this.linePos==1 && typeof(this.areaMapRight)!="undefined"){
      this.navAreaChange(this.areaMapRight);
    }     
    else if(this.pos%this.linePos!=0){
      this.navPosChange(1); 
    }
  },
  keyLeft:function(){   /*区域内向左键值事件*/
    if(this.linePos == 1 && typeof(this.areaMapLeft)!="undefined"){
      this.navAreaChange(this.areaMapLeft);
    }
    else if(this.pos%this.linePos == 1 && typeof(this.areaMapLeft)!="undefined" && this.linePos!=1){
      this.navAreaChange(this.areaMapLeft);
    }   
    else{
      this.navPosChange(-1);
    }
  },
  doSelected:function(){      /*区域内确定键值事件*/
    var strs = ele(this.id).title;      
    var _pageCode=this.id;
    if (strs == undefined || strs == null || strs == "") {
      return
    }
    else{
      eval("var _addressObj = " + strs);
      if (_addressObj != undefined && _addressObj != null && _addressObj.length > 0) {
        var jsName = _addressObj[0].js;
        var strUrl = _addressObj[0].url;
        var jsresult = true;
        if (jsName != undefined && jsName != null && jsName != "") {            
            
          try {
            //refreshLog("",_pageCode);
            setTimeout(function(){jsresult = eval(jsName);},400);
          } catch(e) {
            jsresult = false;
          }
        }
        if (strUrl != undefined && strUrl != null && strUrl != "") {
          try{
            var temp = strUrl.split("?");
            strUrl = temp[0];
            if (temp.length > 1){
              strUrl += "?" + encodeURI(temp[1]);
            }
            var cookieValue=this.id;
            if(typeof(addFocusCookie)!="undefined"&&addFocusCookie!=""&&addFocusCookie!=null){
              cookieValue=cookieValue+addFocusCookie;
              }
            setCookie(cur_cookie,cookieValue);
            delCookieLess(cur_cookie);  
            //refreshLog(strUrl,_pageCode);
          }catch(e){};
          setTimeout(function(){window.location.href = strUrl;},400);
        }
        return jsresult
      }
    }
  },
  comeBack:function(){      /*区域内返回键值事件*/
    if(typeof(cur_cookie)!="undefined") delCookie(cur_cookie);  
    window.history.go(-1);
  },
    keyQuit:function(){
    if(typeof(cur_cookie)!="undefined") delCookie(cur_cookie);  
    window.history.go(-1);
  },

  pageDown:function(){      /*区域内向下翻页事件*/
    if(this.pageNum < this.totlePageNum){
      this.pageNum++;
      this.initList();
    }   
  },
  pageUp:function(){      /*区域内向上翻页事件*/
    if(this.pageNum > 1){
      this.pageNum--;
      this.initList();
    }   
  },
  initList:function(){    /*载入数据事件*/
  },
  showUp:function(){      /*区域内显示向上箭头事件*/   
  },
  showDown:function(){    /*区域内显示向下箭头事件*/
  },
  hidUp:function(){   /*区域内隐藏向上箭头事件*/
  },
  hidDown:function(){ /*区域内隐藏向下箭头事件*/
  }
}
function refreshLog(_cp,_pageCode){
  try{
    var stbid=getCookie("stbid");
    var _projectName=getCookie("cityCode");
    var _iframeDom=ele("jiance");
    var cp=encodeURIComponent(_cp);
    var _iframeUrl=logPath+"?projectName="+_projectName+"&pagecode="+_pageCode+"&cp="+cp+"&rp="+LogReferPage+"&tvcode="+stbid;
    if(_iframeDom==null){
       var iframeDomNew = document.createElement("iframe");
       iframeDomNew.setAttribute("id","jiance");
       iframeDomNew.width=0;
       iframeDomNew.height=0;
       iframeDomNew.src=_iframeUrl;
       document.body.appendChild(iframeDomNew);
       
    }else{
       _iframeDom.src = _iframeUrl; 
    }
  }catch(e){}
}
//获得元素对象
function ele(_elementId){
  var obj = document.getElementById(_elementId);
  if(typeof(obj)!="object"){
    obj = null;
  }
  return obj;
}
//读取样式
function getClass(_elementId){
  var className = "";
  var obj = ele(_elementId);
  if(obj!=null){
   className=obj.className;
  }
  return className;
}
//写样式
function setClass(_elementId,_className){
   var obj = ele(_elementId);
   if(obj!=null){
     obj.className = _className;
   }
}
//添加样式
function addClass(_elementId,_className){
  var oldClassName = getClass(_elementId);
  var newClassName = (oldClassName +" "+ _className).replace(/(^\s*)|(\s*$)/g, "");
  setClass(_elementId,newClassName);
}
//去除样式
function removeClass(_elementId,_className){
  var oldClassName = getClass(_elementId);
  if(oldClassName!=null){
    var reg = new RegExp(_className,"g");
    var newClassName = oldClassName.replace(reg,"").replace(/(^\s*)|(\s*$)/g, "");
    setClass(_elementId,newClassName)
  }
}
//元素读取HTML
function eleGetHtml(_elementId){
   var text = "";
   var obj = ele(_elementId);
   if(obj!=null)
    text=obj.innerHTML;
   return text;
}
//元素写入HTML
function eleSetHtml(_elementId,_text){
   var obj = ele(_elementId);
   if(obj!=null)
     obj.innerHTML = _text;
}
//添加字体
function addHtml(_elementId,_text){
  var oldtext = eleGetHtml(_elementId);
  var newtext = oldtext + _text;
  eleSetHtml(_elementId,newtext);
}

function eleSetLeft(elementId,left){
   var obj = ele(elementId);
   if(obj!=null)
     obj.style.left = left;
}

function eleGetLeft(elementId){
   var left = "";
   var obj = ele(elementId);
   if(obj!=null)
    left=obj.style.left;
   return left;
}
function eleSetTop(elementId,top){
   var obj = ele(elementId);
   if(obj!=null)
     obj.style.top = top;
}

function eleGetTop(elementId){
   var top = "";
   var obj = ele(elementId);
   if(obj!=null)
     top=obj.style.top;
   return top;
}

function setCookie(name,value){ 
  var exp = new Date(); 
  exp.setTime(exp.getTime() + (30*24*60*60*1000));
  window.document.cookie = name + "=" + escape (value) + "; expires=" + exp.toGMTString()+";path=/";
}
function getCookie(sName){
  var aCookie = document.cookie.split("; ");
  for (var i=0; i < aCookie.length; i++)
  {
    var aCrumb = aCookie[i].split("=");
    if (sName == aCrumb[0]){
      return unescape(aCrumb[1]);
    }
  }
  return null;
}
function delCookie(name){ 
  var exp = new Date(); 
  exp.setTime(exp.getTime() -1000);
  window.document.cookie = name + "= null; expires=" + exp.toGMTString()+";path=/";
}

function delCookieLess(curCookie)
{     var strCookie=document.cookie; 
      //将多cookie切割为多个名/值对 
      var arrCookie=strCookie.split("; "); 
      //遍历cookie数组，处理每个cookie对 
      for(var i=0;i<arrCookie.length;i++){ 
      var arr=arrCookie[i].split("=");  
      if(parseInt(arr[0]) < parseInt(curCookie)){ 
        delCookie(arr[0]);
        } 
      } 
}

function delAllCookie()
{     var strCookie=document.cookie; 
      var arrCookie=strCookie.split("; ");
      for(var i=0;i<arrCookie.length;i++){ 
        var arr=arrCookie[i].split("="); 
        var matchCookie = arr[0].match("_cookie");
        if(matchCookie == "_cookie")
        {
          delCookie(arr[0]);
        }
      } 
}
//setIntervalFuncObj:定时对象   timeFuncStr: 定时执行方法（字符串）  time:时间(ms) isFrontRun:是否定时前执行一次
function setIntervalFunction(setIntervalFuncObj,timeFuncStr,time,isFrontRun,frontFunc){


  var tempSetIntervalFuncObj = null;
  clearSetIntervalFunction(setIntervalFuncObj);
  if(typeof(frontFunc)=="function")
    frontFunc();
    if(isFrontRun)
      eval(timeFuncStr);  
    tempSetIntervalFuncObj = setInterval(timeFuncStr,time); //毫秒
    return tempSetIntervalFuncObj;
}
//清理定时方法
function clearSetIntervalFunction(setIntervalFuncObj,endFunc){
  if(setIntervalFuncObj!=null){
  clearInterval(setIntervalFuncObj);
    setIntervalFuncObj = null;
  }
  if(typeof(endFunc)=="function")
    endFunc(); 
}

var scrollA,setTimeArrowUp,setTimeArrowDown,setTimeClock;
function scrollArrow(id,keyword,keywordOn){
    clearInterval(scrollA);
    scrollA = setInterval(function(){
        setClass(id,keywordOn);
        setTimeout(function(){
        setClass(id,keyword);
        },300);
      },1200);

    setTimeout(function(){
      clearInterval(scrollA);
      },3700);

}

function getItemIndex(index,arraylen){
   var maxIndex = arraylen-1;
   if(index<0)
      index = arraylen+index;
   if(index>maxIndex)
       index = index-arraylen;
   return index;    
}

function getNavIdNum(idStr,replaceKey){
  return parseInt(idStr.replace(replaceKey,""),10);
}

function itemMove(num,selectIndex,focusIndex,itemArray,positionArray){
   selectIndex +=num;
   var arraylen = itemArray.length;  
   selectIndex=getItemIndex(selectIndex,arraylen);
   var positionLen = positionArray.length;
   var tempIndex = 0;
   for(var i=0;i<positionLen;i++){
     if(i==focusIndex){
       tempIndex =  selectIndex;
     }else{
       tempIndex = getItemIndex(selectIndex+(i-focusIndex),arraylen);
     }
     eleSetLeft(itemArray[tempIndex],positionArray[i]); 
   }
}

function changeNavIndex(num,navShowIndex,navShowArray,navArray,replaceKey){
    var tempNavShowIndex = navShowIndex+num;
    var navArrayLen = navArray.length;
    var navShowArrayLen = navShowArray.length;
    if(tempNavShowIndex<0){
     var index = getNavIdNum(navShowArray[0],replaceKey);
     index = getItemIndex(index-2,navArrayLen);
     navShowArray.unshift(navArray[index]);
     navShowArray.pop();
     tempNavShowIndex = navShowIndex;
    }else if(tempNavShowIndex>=navShowArrayLen){
     var index = getNavIdNum(navShowArray[navShowArrayLen-1],replaceKey);
     index = getItemIndex(index,navArrayLen);
     navShowArray.push(navArray[index]);
     navShowArray.shift(); 
     tempNavShowIndex = navShowIndex;
    }
    return tempNavShowIndex;
}

/** 文字滚动 */
var scrollWidth;//显示的文字宽度
var scrollTimeout;
var _text_width;
var _text_left = 0;//变化中的text left
var _inc=1;//滚动方向
var _scrollObj;
var _txtFontSize;
function scrollText(scrollObj,_scrollWidth,_txtFontSize){
  var _content = scrollObj.innerHTML;
  _text_width =getTextWeight(_content,_txtFontSize);
  scrollObj.style.left = 0+"px";
  _text_left = 0;
  _scrollObj = scrollObj;
  scrollWidth = _scrollWidth;
  if(_text_width<=scrollWidth){
    stopScroll(_scrollObj);}
  else if(scrollTimeout==null){
    scrollTimeout = setInterval("moveText()",50);
    }
}
//取文字宽度
function getTextWeight(content,_txtFontSize){
  var contentLen= content.length;
        var txtFontSize = _txtFontSize;
  return contentLen*txtFontSize ;
}
//每间隔1s移动text
function moveText(){
  if(-_text_left+scrollWidth==_text_width){

    _inc = -1;
  }else if(_text_left == 0){
    _inc = 1;
  }
  _text_left -=_inc;
  _scrollObj.style.left = _text_left+"px";
}
function stopScroll(scrollObj){
    clearInterval(scrollTimeout);
    scrollTimeout =null;
    scrollObj.style.left = "0px";
  
}
/** end 文字滚动 */
// 调用网厅单个产品订购页,_pValue=PPV,_type=平台类型，_backUrl返回地址
function gotoYYTEpglist(_pValue,_type,_backUrl){
  var rootPath="http://125.210.207.109/product/epglist.do";
  var pValue=_pValue,type=_type;
  var backUrl=encodeURI(_backUrl);
  var gotoUrl=rootPath+"?resourceNo="+getCookie("stbid")+"&type="+_type+"&pType=3&pValue="+pValue+"&backUrl="+backUrl;
  var currentUrl=window.location.href;
  if(currentUrl.indexOf(".hzdtv.tv")<0){
    gotoUrl="/templates/iptvtest/runtime/default/template/index/alert/alert.jsp?id=commer"
  }
  window.location.href=gotoUrl;
}
// 调用网厅产品包订购页,_tagId=指定id,_type=平台类型，_backUrl返回地址
function gotoYYTList(_tagId,_type,_backUrl){
  var rootPath="http://125.210.207.109/product/list.do";
  var tagId=_tagId,type=_type;
  var backUrl=encodeURI(_backUrl);
  var gotoUrl=rootPath+"?resourceNo="+getCookie("stbid")+"&type="+_type+"&tagId="+tagId+"&backUrl="+backUrl;
  var currentUrl=window.location.href;
  if(currentUrl.indexOf(".hzdtv.tv")<0){
    gotoUrl="/templates/iptvtest/runtime/default/template/index/alert/alert.jsp?id=commer"
  }
  window.location.href=gotoUrl;
}
// 调用杭州网厅产品包订购页,_epgCode=门户编码,_type=平台类型，_backUrl返回地址
function gotoYYTIndex(_epgCode,_type,_backUrl){
  var rootPath="http://125.210.207.109/boss-selfpc/index.do";
  var epgCode=_epgCode,type=_type;
  var backUrl=encodeURI(_backUrl);
  var gotoUrl=rootPath+"?resourceNo="+getCookie("stbid")+"&type="+_type+"&epgCode="+_epgCode+"&backUrl="+backUrl;
  var currentUrl=window.location.href;
  if(currentUrl.indexOf(".hzdtv.tv")<0){
    gotoUrl="http://125.210.207.109/index.do"+"?resourceNo="+getCookie("stbid")+"&type="+_type+"&epgCode="+_epgCode+"&backUrl="+backUrl;
  }
  window.location.href=gotoUrl;
}
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); 
  return null;
}
function replaceParamVal(paramName,replaceWith,_url) {
    try{
    var oUrl = _url.toString();
    var re=eval('/('+ paramName+'=)([^&]*)/gi');
    var nUrl = oUrl.replace(re,paramName+'='+replaceWith);
    return nUrl;
  }catch(e){}
}
function getUrlParameterValueByName(curUrl,name) {
    var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
  if (reg.test(curUrl))
    return unescape(RegExp.$2.replace(/\+/g, " "));
  return "";
}
var PeiZiHandleObj = {
  getUrlHttp:null,
  url:"",
  loadStatus:true,
  _callBackFun:"",
  clearAjax:function(){
    if(PeiZiHandleObj.loadStatus==false){
      PeiZiHandleObj.getUrlHttp.abort();
      PeiZiHandleObj.loadStuts = true;
    }
  },
  getPeiZiUrl:function(_obj,_callBackFun){
    try{ 
      if(!_obj.type)return "#";
      PeiZiHandleObj._callBackFun = _callBackFun;
      PeiZiHandleObj.url = "";
      switch(_obj.type){
        case "1":
          if(typeof(PeiZiHandleObj._callBackFun)=="function"){
            PeiZiHandleObj._callBackFun("http://" + document.domain+"/a?f="+_obj.url);
          }
          break;
        case "2":
          if(typeof(PeiZiHandleObj._callBackFun)=="function"){
            var _folderCode=getUrlParameterValueByName(_obj.url,"f");
            var _returnUrl;
            if(_folderCode==null){
              _returnUrl="#";
            }else{
              _returnUrl="/a?f="+_folderCode;
            }
            PeiZiHandleObj._callBackFun(_returnUrl);
          }
          break;
        case "3":
          if(typeof(PeiZiHandleObj._callBackFun)=="function"){
            var _u = "http://125.210.228.101/activity/act.jsp?indexUrl="+getCookie("return_url")+"&tvType=1&regionId=330100&stbId="+getCookie("stbid")+"&returnUrl="+LogReferPage+"&piid="+_obj.url;
            PeiZiHandleObj._callBackFun(_u);
            
          }
          break;
        case "4":
          var url ="";
          if(_obj.url.indexOf("?") > -1){
            url = _obj.url;
            if(_obj.url.indexOf("indexUrl=") > -1){
              url = replaceParamVal("indexUrl",encodeURIComponent(getCookie("return_url")),url);
            }else{
              url = url +"&indexUrl="+encodeURIComponent(getCookie("return_url"));
            }
            if(_obj.url.indexOf("returnUrl=") > -1){
              url = replaceParamVal("returnUrl",LogReferPage,url);
            }else {
              url = url + "&returnUrl="+LogReferPage;
            }
            if(_obj.url.indexOf("stbid=") > -1){
              url = replaceParamVal("stbid",getCookie("stbid"),url);
            }else {
              url = url + "&stbid="+getCookie("stbid");
            }
            if(_obj.url.indexOf("uid=") > -1){
              url = replaceParamVal("uid",getCookie("uid"),url);
            }else{
              url = url + "&uid="+getCookie("uid");
            }
            if(_obj.url.indexOf("cityCode=") > -1){
              url = replaceParamVal("uid",getCookie("cityCode"),url);
            }else{
              url = url + "&cityCode="+getCookie("cityCode");
            } 
          }else{
            url =_obj.url+"?indexUrl="+encodeURIComponent(getCookie("return_url"))+"&returnUrl="+LogReferPage+"&stbid="+getCookie("stbid")+"&uid="+getCookie("uid")+"&cityCode="+getCookie("cityCode");
          }
          if(typeof(PeiZiHandleObj._callBackFun)=="function"){
            PeiZiHandleObj._callBackFun(url);
          }
          break;
        case "5":
          if(typeof(PeiZiHandleObj._callBackFun)=="function"){
            PeiZiHandleObj._callBackFun(_obj.url);
          }
          break;
        case "6":
          if(PeiZiHandleObj.loadStatus==true){
            PeiZiHandleObj.loadStatus = false;
            var DGresultCode = getQueryString("result");
            DGresultCode = (DGresultCode==null || DGresultCode=="null") ? "" : DGresultCode;
            var getUrlAjaxReq = ClpsRootUrl+"/template/jsonData/"+"/getPlayUrl.jsp?"+_obj.url;
            PeiZiHandleObj.getUrlHttp = new XMLHttpRequest();
            PeiZiHandleObj.getUrlHttp.open("GET",getUrlAjaxReq,true);
            PeiZiHandleObj.getUrlHttp.send();
            PeiZiHandleObj.getUrlHttp.onreadystatechange = function(){
              if(PeiZiHandleObj.getUrlHttp.readyState==4 && PeiZiHandleObj.getUrlHttp.status==200){
                var str = PeiZiHandleObj.getUrlHttp.responseText;
                PeiZiHandleObj.loadStatus = true;
                try{var ReqObj = eval ("(" + str + ")");}catch(err){var ReqObj ={};}
                PeiZiHandleObj.url = ReqObj.bottomUrl+"&DGresultCode="+DGresultCode;
                if(typeof(PeiZiHandleObj._callBackFun)=="function"){
                  PeiZiHandleObj._callBackFun(PeiZiHandleObj.url);
                }
              }
            }
          }
          break;  
        default:
          if(_obj.url==null || typeof(_obj.url)=="undefined" || _obj.url=="")_obj.url="#";
          if(typeof(PeiZiHandleObj._callBackFun)=="function"){
            PeiZiHandleObj._callBackFun(_obj.url);
          }
      }
    }catch(e){}
  },
  
  getPeiZiText:function(_obj){
    try{ 
      var text = "";
      if(_obj.text && _obj.text!="" && typeof(_obj.text)!="undefined")text = _obj.text;
      return text;
    }catch(e){}
  },
  getPeiZiImg:function(_obj){
    try{ 
      var img = "";
      if(_obj.img && _obj.img!="" && typeof(_obj.img)!="undefined")img = _obj.img;
      return img;
    }catch(e){}
  }
}




///////////////////////////////////////////////////////////////////////////////////////////////////////////////
function TextAJAX(_url,_callBack)
{
	var xmlhttp;
	if (window.XMLHttpRequest){
		xmlhttp=new XMLHttpRequest();
	}
	else{
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}	
	xmlhttp.open("GET",_url,true);
	xmlhttp.send();
						
	xmlhttp.onreadystatechange=function(){
		if (xmlhttp.readyState==4 && ( xmlhttp.status==200 || xmlhttp.status==0)){
			_callBack(xmlhttp.responseText)
		}
	}
}