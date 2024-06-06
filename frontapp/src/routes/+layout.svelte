<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import '$lib/app.css';
	import { untrack } from 'svelte';
	import { writable } from 'svelte/store';
	const { children } = $props();
	let sse: EventSource;
	let cache = writable(0);
	rq.effect(async () => {
		untrack(() => {
			rq.initAuth();
		});
		if (rq.isLogout()) {
			if (sse) {
				sse.close();
			}
		} else {
			sse = new EventSource(
				`${import.meta.env.VITE_CORE_API_BASE_URL}/api/sse/subscribe/login?memberId=${rq.member.id}`
			);

			sse.addEventListener('updatePoints', (e) => {
				rq.member.cache = JSON.parse(e.data).cache;
			});
		}
	});
</script>

<header class="navbar bg-gray-50 shadow fixed top-0 left-0 right-0 text-blue-400 z-10">
	<div class="flex-1">
		<div class="flex-none">
			<div class="dropdown">
				<div tabindex="0" role="button" class="btn btn-ghost btn-circle">
					<svg
						xmlns="http://www.w3.org/2000/svg"
						class="h-5 w-5"
						fill="none"
						viewBox="0 0 24 24"
						stroke="currentColor"
						><path
							stroke-linecap="round"
							stroke-linejoin="round"
							stroke-width="2"
							d="M4 6h16M4 12h16M4 18h7"
						/></svg
					>
				</div>
				<ul
					tabindex="0"
					class="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52 gap-y-1"
				>
					{#if rq.isAdmin()}
						<li>
							<a href="/adm" class="font-semi-bold"
								><i class="fa-solid fa-right-to-bracket"></i> 관리자</a
							>
						</li>
					{/if}
					{#if rq.isLogout()}
						<li>
							<a class="font-semi-bold" href="/login"
								><i class="fa-solid fa-right-to-bracket"></i> 로그인</a
							>
						</li>
					{/if}
					{#if rq.isLogin()}
						<li>
							<a class="font-semi-bold" href="/image"
								><svg
									xmlns="http://www.w3.org/2000/svg"
									viewBox="0 0 16 16"
									fill="currentColor"
									class="w-4 h-4"
								>
									<path
										fill-rule="evenodd"
										d="M2 4a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V4Zm10.5 5.707a.5.5 0 0 0-.146-.353l-1-1a.5.5 0 0 0-.708 0L9.354 9.646a.5.5 0 0 1-.708 0L6.354 7.354a.5.5 0 0 0-.708 0l-2 2a.5.5 0 0 0-.146.353V12a.5.5 0 0 0 .5.5h8a.5.5 0 0 0 .5-.5V9.707ZM12 5a1 1 0 1 1-2 0 1 1 0 0 1 2 0Z"
										clip-rule="evenodd"
									/>
								</svg>
								사진(이미지) 등록</a
							>
						</li>
						<li>
							<a class="font-semi-bold" href="/like/{rq.member.id}"
								><svg
									xmlns="http://www.w3.org/2000/svg"
									viewBox="0 0 16 16"
									fill="currentColor"
									class="w-4 h-4"
								>
									<path
										fill-rule="evenodd"
										d="M2 4a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V4Zm10.5 5.707a.5.5 0 0 0-.146-.353l-1-1a.5.5 0 0 0-.708 0L9.354 9.646a.5.5 0 0 1-.708 0L6.354 7.354a.5.5 0 0 0-.708 0l-2 2a.5.5 0 0 0-.146.353V12a.5.5 0 0 0 .5.5h8a.5.5 0 0 0 .5-.5V9.707ZM12 5a1 1 0 1 1-2 0 1 1 0 0 1 2 0Z"
										clip-rule="evenodd"
									/>
								</svg>
								좋아요 한 사진/이미지</a
							>
						</li>
						{#if !rq.isAdmin()}
							<li>
								<a class="font-semi-bold" href="/myimage"
									><i class="fa-regular fa-circle-question"></i> 내 이미지
								</a>
							</li>
						{/if}
						<li class="font-semi-bold">
							<button on:click={() => rq.logout()}>
								<i class="fa-solid fa-right-from-bracket"></i> 로그아웃
							</button>
						</li>
						<hr />
						<div class="flex justify-between font-semi-bold mx-3">
							<span>내 캐쉬:</span>
							<span>{rq.member.cache} 캐쉬</span>
						</div>
					{/if}
				</ul>
			</div>
		</div>
		<div class="flex-1"></div>
	</div>
	<div class="flex-1 flex justify-center">
		<a href="/" class="cookie-regular text-4xl">WHEV</a>
	</div>

	<div class="flex-1 justify-end mr-4">
		<div class="flex gap-x-4 relative items-center">
			<button
				on:click={() => {
					rq.goTo(`/member/mypage/alarm`);
				}}
			>
				<svg
					xmlns="http://www.w3.org/2000/svg"
					fill="none"
					viewBox="0 0 24 24"
					stroke-width="1.5"
					stroke="currentColor"
					class="w-6 h-6"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"
					/>
				</svg>
			</button>
		</div>
	</div>
</header>

<main>{@render children()}</main>
