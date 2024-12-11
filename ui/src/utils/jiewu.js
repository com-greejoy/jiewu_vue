import printJS from 'print-js';

export function getOrderDesc(dataLength, playerGroup, haiXuanOrder) {
  let orderText = "";
  if (!haiXuanOrder) {
    return "-";
  }

  haiXuanOrder = haiXuanOrder * 1;
  if (playerGroup.includes("团体")) {
    // 团体精英
    if (playerGroup.includes("精英")) {
      if (haiXuanOrder <= 3) {
        orderText = "第" + convertToChineseNumeral(haiXuanOrder) + "名";
      }else {
        // if((playerGroup.includes("499") || playerGroup.includes("504")) && haiXuanOrder == 4){
        //     orderText = "二等奖";
        // }else{
        dataLength = dataLength - 3;
        haiXuanOrder = haiXuanOrder - 3;
        if (haiXuanOrder <= Math.round(dataLength * 0.4)) {
          orderText = "二等奖";
        }
        if (haiXuanOrder > Math.round(dataLength * 0.4) ) {
          orderText = "三等奖";
        }
        // }
      }

    }else if (playerGroup.includes("进阶")) {
      if (haiXuanOrder <= 3) {
        orderText = "第" + convertToChineseNumeral(haiXuanOrder) + "名";
      }else {
        // if((playerGroup.includes("499") || playerGroup.includes("504")) && haiXuanOrder == 4){
        //     orderText = "二等奖";
        // }else{
        dataLength = dataLength - 3;
        haiXuanOrder = haiXuanOrder - 3;
        if (haiXuanOrder <= Math.round(dataLength * 0.4)) {
          orderText = "二等奖";
        }
        if (haiXuanOrder > Math.round(dataLength * 0.4) ) {
          orderText = "三等奖";
        }
        // }
      }

    }else{
      // 团体新星 3  4  3
      if (haiXuanOrder <= Math.round(dataLength * 0.3)) {
        orderText = "一等奖";
      }
      if (haiXuanOrder > Math.round(dataLength * 0.3) && haiXuanOrder <= Math.round(dataLength * 0.7)) {
        orderText = "二等奖";
      }
      if (haiXuanOrder > Math.round(dataLength * 0.7)) {
        orderText = "三等奖";
      }

      // if(playerGroup.includes("495")){
      //     orderText = "一等奖";
      // }
    }
  }else{
    // 单人精英
    if (playerGroup.includes("精英")) {
      if (haiXuanOrder <= 4) {
        orderText = "第" + convertToChineseNumeral(haiXuanOrder) + "名";
        if (haiXuanOrder == 1) {
          orderText = "冠军";
        }else if (haiXuanOrder == 2) {
          orderText = "亚军";
        }else if (haiXuanOrder == 3) {
          orderText = "季军";
        }
      }else {
        dataLength = dataLength - 4;
        haiXuanOrder = haiXuanOrder - 4;
        if (haiXuanOrder <= Math.round(dataLength * 0.4)) {
          orderText = "二等奖";
        }
        if (haiXuanOrder > Math.round(dataLength * 0.4) ) {
          orderText = "三等奖";
        }
        if(haiXuanOrder == 1){
          orderText = "二等奖";
        }
      }
    }else{
      // 单人新星  3  4  3
      if (haiXuanOrder <= Math.round(dataLength * 0.3)) {
        orderText = "一等奖";
      }
      if (haiXuanOrder > Math.round(dataLength * 0.3) && haiXuanOrder <= Math.round(dataLength * 0.7)) {
        orderText = "二等奖";
      }
      if (haiXuanOrder > Math.round(dataLength * 0.7)) {
        orderText = "三等奖";
      }
    }
  }
  return orderText;
}

export function elementToImage(selector, filename) {
  // 获取元素
  var element = document.querySelector(selector);
  // 创建canvas
  var canvas = document.createElement('canvas');
  canvas.width = element.offsetWidth;
  canvas.height = element.offsetHeight;
  var ctx = canvas.getContext('2d');
  // 将元素绘制到canvas上
  var data = '<svg xmlns="http://www.w3.org/2000/svg" width="' + canvas.width + '" height="' + canvas.height + '">';
  data += '<foreignObject width="100%" height="100%">';
  data += '<style type="text/css">body { margin: 0; padding: 0; }</style>';
  data += '<body xmlns="http://www.w3.org/1999/xhtml">' + element.innerHTML + '</body>';
  data += '</foreignObject>';
  data += '</svg>';
  var DOMURL = window.URL || window.webkitURL || window;
  var img = new Image();
  var svg = new Blob([data], {type: 'image/svg+xml;charset=utf-8'});
  var url = DOMURL.createObjectURL(svg);

  img.onload = function() {
    ctx.drawImage(img, 0, 0);
    DOMURL.revokeObjectURL(url);
    // 下载图片
    var imgURI = canvas.toDataURL('image/png').replace('image/png', 'image/octet-stream');
    var link = document.createElement('a');
    link.href = imgURI;
    link.download = filename + '.png';
    link.click();
  };
  img.src = url;
}

export function handlePrint(id) {
  printJS({
    printable: id,
    maxWidth: "800",
    type: 'html',
    //为了样式生效需要添加 targetStyles:['*'] 和 font_size:''
    targetStyles: ['*'],
    font_size: '',
    //margin0 默认打印页边距为0
    style: `
          @page {
            size:auto;
            margin: 16;
          }
        `
  });
}

export function convertToChineseNumeral(num) {
  num = num || ""
  if (num == 10) {
    return '十'
  } else if (num == 1) {
    return '一'
  }
  const digits = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'];
  const units = ['', '十', '百', '千', '万'];
  let result = '';
  let numStr = num.toString();
  for (let i = 0; i < numStr.length; i++) {
    let digit = parseInt(numStr.charAt(i));
    let unit = units[numStr.length - i - 1];
    if (digit === 0) {
      // 当前数字为0时不需要输出汉字，但需要考虑上一个数字是否为0，避免出现连续的零
      if (result.charAt(result.length - 1) !== '零') {
        result += '零';
      }
    } else {
      result += digits[digit] + unit;
    }
  }
  // 对于一些特殊的数字，如10、100等，需要在最前面加上“一”
  if (result.charAt(0) === '一') {
    result = result.substr(1, result.length);
  } else if (result.charAt(0) === '百') {
    result = '一' + result;
  } else if (result.charAt(0) === '千') {
    result = '一' + result;
  }
  return result;
};
