<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import '$lib/app.css';
	import { untrack } from 'svelte';
	const { children } = $props();
	rq.effect(async () => {
		untrack(() => {
			rq.initAuth();
		});
	});
</script>

<header class="navbar bg-gray-50 shadow">
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
					class="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52"
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
							<a class="font-semi-bold" href="/board/myList"
								><i class="fa-solid fa-list-check"></i> 내 Q&A</a
							>
						</li>
						{#if !rq.isAdmin()}
							<li>
								<a class="font-semi-bold" href="/qna"
									><i class="fa-regular fa-circle-question"></i> 1대1 문의</a
								>
							</li>
						{/if}
						<li class="font-semi-bold">
							<button on:click={() => rq.logout()}>
								<i class="fa-solid fa-right-from-bracket"></i> 로그아웃
							</button>
						</li>
					{/if}
				</ul>
			</div>
		</div>
		<div class="flex-1"></div>
	</div>
	<div class="flex-1 flex justify-center">
		<a href="/" class="font-bold">EduBridge</a>
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
