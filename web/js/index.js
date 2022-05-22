function delfruit(fid) {
    if (confirm("是否确认删除吗")) {
        window.location.href = 'del.do?fid=' + fid;
    }
}

function page(pageNo) {
    window.location.href = '111?pageNo=' + pageNo;
}