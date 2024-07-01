<script>
	export let selectedSection = '사용자';
</script>

<aside class="sidebar bg-gray-800 text-white h-screen p-4">
	<div class="mb-4">
		<h1 class="text-2xl font-bold">DDV</h1>
		<p class="text-sm">Administration</p>
	</div>
	<nav>
		<ul>
			<li class="mb-2">
				<h2 class="font-bold">회원 관리</h2>
				<ul class="ml-4">
					<li class:text-orange-500={selectedSection === '사용자'}>사용자</li>
					<li class:text-orange-500={selectedSection === '관리자'}>관리자</li>
					<li class:text-orange-500={selectedSection === '문의 내역'}>문의 내역</li>
				</ul>
			</li>
			<li class="mb-2">
				<h2 class="font-bold">콘텐츠 관리</h2>
				<ul class="ml-4">
					<li class:text-orange-500={selectedSection === '서베이'}>서베이 (Survey)</li>
					<li class:text-orange-500={selectedSection === '퀵폴'}>퀵폴 (Poll)</li>
					<li class:text-orange-500={selectedSection === 'FAQ'}>자주 묻는 질문 (FAQ)</li>
				</ul>
			</li>
			<li class="mb-2">
				<h2 class="font-bold">인사이트 분석</h2>
				<ul class="ml-4">
					<li class:text-orange-500={selectedSection === '서베이 참여 횟수'}>서베이 참여 횟수</li>
					<li class:text-orange-500={selectedSection === '퀵폴 참여 횟수'}>퀵폴 참여 횟수</li>
					<li class:text-orange-500={selectedSection === '포인트 획득 순위'}>
						사용자 포인트 획득 순위
					</li>
				</ul>
			</li>
			<li class="mt-4">
				<button class="text-red-500">로그아웃 하기</button>
			</li>
		</ul>
	</nav>
</aside>

<style>
	.sidebar {
		width: 200px;
	}
</style>
