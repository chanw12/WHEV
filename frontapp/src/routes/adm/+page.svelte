<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';
	import AdmNav from '../../lib/components/AdmNav.svelte';
	import { onMount } from 'svelte';

	let reportList: components['schemas']['ReportDto'][] = $state();

	async function load() {
		if (import.meta.env.SSR) throw new Error('CSR ONLY');

		const isMobileResponse = await rq.apiEndPoints().GET(`/api/v1/admin/deviceCheck`);
		const { isMobile } = isMobileResponse.data?.data!;

		if (isMobile) {
			rq.msgError('관리자 페이지는 pc로 접속 바랍니다.');
			rq.goTo('/');
			return;
		}

		const isAdminResponse = await rq.apiEndPoints().GET(`/api/v1/members/isAdmin`);
		const { isAdmin } = isAdminResponse.data?.data!;
		const isLoginResponse = await rq.apiEndPoints().GET(`/api/v1/members/isLogin`);
		const { isLogin } = isLoginResponse.data?.data!;
		if (!isAdmin && isLogin) {
			rq.msgError('관리자 권한이 없습니다');
			rq.goTo('/');
			return;
		}
		if (!isAdmin && !isLogin) {
			rq.msgWarning('관리자 로그인 후 이용 해 주세요');
			rq.goTo('/login');
			return;
		}

		const reportedImages = await rq.apiEndPoints().GET(`/api/v1/admin/reports/list`);
		console.log(reportedImages);
		reportList = reportedImages.data?.data;

		return { reportList };
	}
</script>

{#await load()}
	<p class="text-center">loading...</p>
{:then { reportList }}
	<div class="flex mt-16">
		<div>
			<AdmNav></AdmNav>
		</div>
		<div class="container mx-auto p-4">
			<div class="grid grid-cols-2 gap-4">
				<!-- Section 1 -->
				<div class="bg-white rounded-lg shadow-lg p-4">
					<div class="flex justify-between items-center mb-4">
						<h2 class="text-xl font-semibold">신고 관리</h2>
						<button class="bg-blue-500 text-white px-2 py-1 rounded">+</button>
					</div>
					<table class="min-w-full bg-white">
						<thead>
							<tr>
								<th class="py-2 px-4 border-b border-gray-200">글/강좌 번호</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 타입</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 사유</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 날짜</th>
							</tr>
						</thead>
						<tbody>
							{#each reportList as report}
								<tr>
									<td class="py-2 px-4 border-b border-gray-200 text-blue-500"
										>Post/{report.imageId}</td
									>
									<td class="py-2 px-4 border-b border-gray-200">{report.reporterid}</td>
									<td class="py-2 px-4 border-b border-gray-200">{report.reason}</td>
									<td class="py-2 px-4 border-b border-gray-200">2024년 3월 27일</td>
								</tr>
							{/each}
						</tbody>
					</table>
				</div>

				<!-- Section 2 -->
				<div class="bg-white rounded-lg shadow-lg p-4">
					<div class="flex justify-between items-center mb-4">
						<h2 class="text-xl font-semibold">신고 관리</h2>
						<button class="bg-blue-500 text-white px-2 py-1 rounded">+</button>
					</div>
					<table class="min-w-full bg-white">
						<thead>
							<tr>
								<th class="py-2 px-4 border-b border-gray-200">글/강좌 번호</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 타입</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 사유</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 날짜</th>
							</tr>
						</thead>
						<tbody>
							{#each reportList as report}
								<tr>
									<td class="py-2 px-4 border-b border-gray-200 text-blue-500"
										>Post/{report.imageId}</td
									>
									<td class="py-2 px-4 border-b border-gray-200">{report.reporterid}</td>
									<td class="py-2 px-4 border-b border-gray-200">{report.reason}</td>
									<td class="py-2 px-4 border-b border-gray-200">2024년 3월 27일</td>
								</tr>
							{/each}
						</tbody>
					</table>
				</div>

				<!-- Section 3 -->
				<div class="bg-white rounded-lg shadow-lg p-4">
					<div class="flex justify-between items-center mb-4">
						<h2 class="text-xl font-semibold">신고 관리</h2>
						<button class="bg-blue-500 text-white px-2 py-1 rounded">+</button>
					</div>
					<table class="min-w-full bg-white">
						<thead>
							<tr>
								<th class="py-2 px-4 border-b border-gray-200">글/강좌 번호</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 타입</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 사유</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 날짜</th>
							</tr>
						</thead>
						<tbody>
							{#each reportList as report}
								<tr>
									<td class="py-2 px-4 border-b border-gray-200 text-blue-500"
										>Post/{report.imageId}</td
									>
									<td class="py-2 px-4 border-b border-gray-200">{report.reporterid}</td>
									<td class="py-2 px-4 border-b border-gray-200">{report.reason}</td>
									<td class="py-2 px-4 border-b border-gray-200">2024년 3월 27일</td>
								</tr>
							{/each}
						</tbody>
					</table>
				</div>

				<!-- Section 4 -->
				<div class="bg-white rounded-lg shadow-lg p-4">
					<div class="flex justify-between items-center mb-4">
						<h2 class="text-xl font-semibold">신고 관리</h2>
						<button class="bg-blue-500 text-white px-2 py-1 rounded">+</button>
					</div>
					<table class="min-w-full bg-white">
						<thead>
							<tr>
								<th class="py-2 px-4 border-b border-gray-200">글/강좌 번호</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 타입</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 사유</th>
								<th class="py-2 px-4 border-b border-gray-200">신고 날짜</th>
							</tr>
						</thead>
						<tbody>
							{#each reportList as report}
								<tr>
									<td class="py-2 px-4 border-b border-gray-200 text-blue-500"
										>Post/{report.imageId}</td
									>
									<td class="py-2 px-4 border-b border-gray-200">{report.reporterid}</td>
									<td class="py-2 px-4 border-b border-gray-200">{report.reason}</td>
									<td class="py-2 px-4 border-b border-gray-200"
										>{`${new Date(report.createDate).getFullYear()}년 ${new Date(report.createDate).getMonth() + 1}월 ${new Date(report.createDate).getDate()}일`}</td
									>
								</tr>
							{/each}
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
{/await}
