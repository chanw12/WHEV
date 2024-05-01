<script lang="ts">
	import { onMount } from 'svelte';
	import axios from 'axios';
	import Modal from '$lib/components/Modal.svelte';
	import type { components } from '$lib/types/api/v1/schema';
	import rq from '$lib/rq/rq.svelte';

	let images1: components['schemas']['ImageDto'][] = [];
	let images2: components['schemas']['ImageDto'][] = [];
	let images3: components['schemas']['ImageDto'][] = [];
	let images4: components['schemas']['ImageDto'][] = [];
	let page = 1;
	let isLoading = false;
	let images: components['schemas']['ImageDto'][] = [];
	let showModal = false;
	let modalImg: components['schemas']['ImageDto'];
	let modaltags: string[] = [];
	let modalImgVote = true;
	let comments = [];
	let comment = '';
	let sse: EventSource;
	let commentEditOpen = 0;
	$: if (!showModal) {
		if (sse) {
			sse.close();
		}
	}
	async function loadComments() {
		const response = await rq.apiEndPoints().GET(`/api/v1/comment/get`, {
			params: {
				query: {
					imageId: modalImg.id
				}
			}
		});
		comments = response.data?.data!;
	}
	async function loadImages() {
		let tempImages1: components['schemas']['ImageDto'][] = [...images1];
		let tempImages2: components['schemas']['ImageDto'][] = [...images2];
		let tempImages3: components['schemas']['ImageDto'][] = [...images3];
		let tempImages4: components['schemas']['ImageDto'][] = [...images4];
		const response = await axios.get(
			import.meta.env.VITE_CORE_API_BASE_URL + '/api/v1/image?page=' + page
		);
		const newImages = response.data.data.items.content;
		if (newImages.length == 0) {
			isLoading = false;
			return;
		}
		for (let i = 0; i < newImages.length; i++) {
			const imageObject: components['schemas']['ImageDto'] = {
				content: newImages[i].content,
				id: newImages[i].id,
				member_id: newImages[i].member_id,
				path: import.meta.env.VITE_CORE_API_BASE_URL + '/gen' + newImages[i].path,
				tags: newImages[i].tags,
				member_nickname: newImages[i].member_nickname,
				date: newImages[i].date
			};

			switch (i % 4) {
				case 0:
					tempImages1.push(imageObject);
					break;
				case 1:
					tempImages2.push(imageObject);
					break;
				case 2:
					tempImages3.push(imageObject);
					break;
				case 3:
					tempImages4.push(imageObject);
					break;
			}
		}
		images1 = tempImages1;
		images2 = tempImages2;
		images3 = tempImages3;
		images4 = tempImages4;

		images = [...images, ...newImages];
		page++;
	}

	onMount(() => {
		loadImages();
		window.addEventListener('scroll', () => {
			if (window.innerHeight + window.scrollY >= document.documentElement.scrollHeight) {
				if (isLoading == false) {
					loadImages();
				}
			}
		});
	});
	async function showModalFnc(img: components['schemas']['ImageDto']) {
		showModal = true;
		modalImg = img;
		modaltags = img.tags.split(',');
		loadComments();
		sse = new EventSource(
			`${import.meta.env.VITE_CORE_API_BASE_URL}/api/sse/subscribe?imageId=${img.id}&memberId=${rq.member.id}`
		);
		sse.addEventListener('addComment', (e) => {
			const newComment = JSON.parse(e.data);
			comments = [newComment, ...comments];
		});

		const { data, error } = await rq.apiEndPointsWithFetch(fetch).GET('/api/v1/image/vote/isvote', {
			params: {
				query: {
					imageId: img.id
				}
			}
		});
		console.log(data.data);
		if (data) {
			modalImgVote = data.data;
		}
	}
	async function saveComment() {
		const { data, error } = await rq.apiEndPointsWithFetch(fetch).POST('/api/v1/comment/save', {
			body: {
				content: comment,
				imageId: modalImg.id
			}
		});
		if (data) {
			rq.msgInfo('댓글이 등록되었습니다');
			comment = '';
		}
	}

	async function voteImage() {
		modalImgVote = !modalImgVote;
		const { data, error } = await rq.apiEndPointsWithFetch(fetch).PUT('/api/v1/image/vote', {
			params: {
				query: {
					imageId: modalImg.id
				}
			}
		});
		if (data) {
			rq.msgInfo('투표가 완료되었습니다');
		}
	}
</script>

{#if isLoading}
	<p>Loading...</p>
{:else}
	<Modal bind:showModal>
		<div class="flex w-full h-4/5 mx-auto my-10 overflow-auto gap-20">
			{#if modalImg && modalImg.path}
				<div class="w-1/2">
					<img class="object-cover h-full w-full" src={modalImg.path} alt="" />
				</div>
				<div class="w-1/2 p-4">
					<div class="flex justify-between items-center mb-4">
						<div class="flex gap-2">
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
									d="M9.568 3H5.25A2.25 2.25 0 0 0 3 5.25v4.318c0 .597.237 1.17.659 1.591l9.581 9.581c.699.699 1.78.872 2.607.33a18.095 18.095 0 0 0 5.223-5.223c.542-.827.369-1.908-.33-2.607L11.16 3.66A2.25 2.25 0 0 0 9.568 3Z"
								/>
								<path stroke-linecap="round" stroke-linejoin="round" d="M6 6h.008v.008H6V6Z" />
							</svg>

							<div class="tags text-sm text-gray-500">
								{#each modaltags as tag}
									<span class="mx-1">#{tag}</span>
								{/each}
							</div>
						</div>

						<div>
							<button class="btn mr-2" on:click={voteImage}>
								{#if !modalImgVote}
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
											d="M6.633 10.25c.806 0 1.533-.446 2.031-1.08a9.041 9.041 0 0 1 2.861-2.4c.723-.384 1.35-.956 1.653-1.715a4.498 4.498 0 0 0 .322-1.672V2.75a.75.75 0 0 1 .75-.75 2.25 2.25 0 0 1 2.25 2.25c0 1.152-.26 2.243-.723 3.218-.266.558.107 1.282.725 1.282m0 0h3.126c1.026 0 1.945.694 2.054 1.715.045.422.068.85.068 1.285a11.95 11.95 0 0 1-2.649 7.521c-.388.482-.987.729-1.605.729H13.48c-.483 0-.964-.078-1.423-.23l-3.114-1.04a4.501 4.501 0 0 0-1.423-.23H5.904m10.598-9.75H14.25M5.904 18.5c.083.205.173.405.27.602.197.4-.078.898-.523.898h-.908c-.889 0-1.713-.518-1.972-1.368a12 12 0 0 1-.521-3.507c0-1.553.295-3.036.831-4.398C3.387 9.953 4.167 9.5 5 9.5h1.053c.472 0 .745.556.5.96a8.958 8.958 0 0 0-1.302 4.665c0 1.194.232 2.333.654 3.375Z"
										/>
									</svg>
								{:else}
									<svg
										xmlns="http://www.w3.org/2000/svg"
										viewBox="0 0 24 24"
										fill="currentColor"
										class="w-6 h-6"
									>
										<path
											d="M7.493 18.5c-.425 0-.82-.236-.975-.632A7.48 7.48 0 0 1 6 15.125c0-1.75.599-3.358 1.602-4.634.151-.192.373-.309.6-.397.473-.183.89-.514 1.212-.924a9.042 9.042 0 0 1 2.861-2.4c.723-.384 1.35-.956 1.653-1.715a4.498 4.498 0 0 0 .322-1.672V2.75A.75.75 0 0 1 15 2a2.25 2.25 0 0 1 2.25 2.25c0 1.152-.26 2.243-.723 3.218-.266.558.107 1.282.725 1.282h3.126c1.026 0 1.945.694 2.054 1.715.045.422.068.85.068 1.285a11.95 11.95 0 0 1-2.649 7.521c-.388.482-.987.729-1.605.729H14.23c-.483 0-.964-.078-1.423-.23l-3.114-1.04a4.501 4.501 0 0 0-1.423-.23h-.777ZM2.331 10.727a11.969 11.969 0 0 0-.831 4.398 12 12 0 0 0 .52 3.507C2.28 19.482 3.105 20 3.994 20H4.9c.445 0 .72-.498.523-.898a8.963 8.963 0 0 1-.924-3.977c0-1.708.476-3.305 1.302-4.666.245-.403-.028-.959-.5-.959H4.25c-.832 0-1.612.453-1.918 1.227Z"
										/>
									</svg>
								{/if}
							</button>
						</div>
					</div>
					<div class="author text-lg font-bold">Author: {modalImg.member_nickname}</div>
					<div class="date text-sm text-gray-500">
						Date: {new Date(modalImg.date).toLocaleDateString('ko-KR')}
					</div>
					<div class="description mt-2 text-gray-700">
						{modalImg.content}
					</div>
					<div class="comments mt-4">
						<h3 class="text-lg font-bold">Comments</h3>
						<form class="mt-2" on:submit={saveComment}>
							<input
								bind:value={comment}
								class="input input-bordered w-full"
								type="text"
								placeholder="Add a comment..."
							/>
							<button class="btn btn-primary mt-2" type="submit">Post</button>
						</form>
						{#each comments as comment}
							<div id="comment__{comment.commentId}" class="">
								<div class="border-b rounded-sm flex justify-between">
									<div>
										<div class="flex items-center">
											<div class="ml-5">
												<span class="font-bold mr-2">{comment.memberName}</span>
											</div>
											<div>
												<p class="text-nm space-y-1.5 p-6">
													{(() => {
														const now = new Date();
														const commentDate = new Date(comment.createDate);
														const seconds = Math.floor((now - commentDate) / 1000);

														let interval = seconds / 31536000;
														if (interval > 1) {
															return Math.floor(interval) + '년 전';
														}
														interval = seconds / 2592000;
														if (interval > 1) {
															return Math.floor(interval) + '개월 전';
														}
														interval = seconds / 86400;
														if (interval > 1) {
															return Math.floor(interval) + '일 전';
														}
														interval = seconds / 3600;
														1;
														if (interval > 1) {
															return Math.floor(interval) + '시간 전';
														}
														interval = seconds / 60;
														if (interval > 1) {
															return Math.floor(interval) + '분 전';
														}
														return Math.floor(seconds) + '초 전';
													})()}
												</p>
											</div>
											<div class="flex justify-end flex gap-2 text-gray-400">
												{#if rq.member.id == comment.memberId || rq.isAdmin()}
													<button class="text-xs">수정</button>
													<p>/</p>
												{/if}
												{#if rq.member.id == comment.memberId || rq.isAdmin()}
													<div>
														<button class="text-xs">삭제</button>
													</div>
												{/if}
											</div>
										</div>
										<div class="flex items-center mx-5 mb-5">
											<span class="text-gray-600">{comment.content}</span>
										</div>
									</div>
								</div>
								<!-- <div class="flex items-center mr-5">
						  <button
							class="btn btn-outline hover:bg-gray-100 hover:text-black flex-col h-14"
							
						  >
							{#if commentLikedByCurrentUser[comments.indexOf(comment)]}
							  <svg
								xmlns="http://www.w3.org/2000/svg"
								fill="red"
								viewBox="0 0 24 24"
								stroke-width="1.5"
								stroke="red"
								class="w-6 h-6"
							  >
								<path
								  stroke-linecap="round"
								  stroke-linejoin="round"
								  d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
								/>
							  </svg>
							{:else}
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
								  d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
								/>
							  </svg>
							{/if}
							<span>{commentLikedNum[comments.indexOf(comment)]}</span>
						  </button>
						</div> -->
							</div>
						{/each}
					</div>
				</div>
			{/if}
		</div>
	</Modal>
	<div class="container mx-auto items-center mt-20">
		<div class="grid grid-cols-2 md:grid-cols-4 gap-4">
			<div class="grid gap-4">
				{#each images1 as image}
					<div>
						<img
							on:click={() => showModalFnc(image)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images2 as image}
					<div>
						<img
							on:click={() => showModalFnc(image)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images3 as image}
					<div>
						<img
							on:click={() => showModalFnc(image)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images1 as image}
					<div>
						<img
							on:click={() => showModalFnc(image)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
					</div>
				{/each}
			</div>
		</div>
	</div>
{/if}
