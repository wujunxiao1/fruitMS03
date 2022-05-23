function delfruit(fid) {
    if (confirm("是否确认删除吗")) {
        window.location.href = 'fruit.do?fid=' + fid + "&operate = delete";
    }
}

function page(pageNo) {
    window.location.href = 'fruit.do?pageNo=' + pageNo;
}