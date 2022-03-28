<#import "/inc/layout.ftl" as Layout>

<@Layout.layout "我的收藏">

<div id="app" class="row justify-content-md-center">

	<#if searchTip><div class="alert alert-info" role="alert">${searchTip}</div></#if>

	<!---->
	<div class="col" id="collects-col">

        <#include '/inc/collect-tpl.ftl'>
		<div class="row" id="masonry"></div>

	</div>

	<script>

		var laytpl, flow
		layui.use(['laytpl', 'flow'], function () {
			laytpl = layui.laytpl;
			flow = layui.flow;
		});
		function flowload(keyword, userId) {
			flow.load({
				elem: '#masonry'
				, isAuto: false
				, end: '哥，这回真的没了~'
				, done: function (page, next) {
					$.get('/api/collects/square', {
						page: page,
						size: 10,
						q: keyword,
						userId: userId
					}, function (res) {
						var lis = [];
						var gettpl = $('#collect-card-tpl').html();
						laytpl(gettpl).render(res.data, function (html) {
							$(".layui-flow-more").before(html);
						});
						next(lis.join(''), page < res.data.totalPages);
					})
				}
			});
		}
		function handleDel(id) {
			layer.confirm('是否确认删除?', function (index) {
				$.post('/api/collect/delete?id=' + id, function (res) {
					if (res.code == 0) {
						$('#masonry-item-' + id).remove()
					}
					layer.msg(res.mess)
				})
				layer.close(index);
			});
		}
		$(function () {
			flowload('${q}', ${userId})
		});

	</script>

</div>

</@Layout.layout>