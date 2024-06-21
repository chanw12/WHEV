<script lang="ts">
	import { onMount } from 'svelte';
	import axios from 'axios';
	import Modal from '$lib/components/Modal.svelte';
	import type { components } from '$lib/types/api/v1/schema';
	import rq from '$lib/rq/rq.svelte';
	import { writable } from 'svelte/store';
	let images1: components['schemas']['ImageDto'][] = [];
	let images2: components['schemas']['ImageDto'][] = [];
	let images3: components['schemas']['ImageDto'][] = [];
	let images4: components['schemas']['ImageDto'][] = [];
	let images1Liked: boolean[] = [];
	let images2Liked: boolean[] = [];
	let images3Liked: boolean[] = [];
	let images4Liked: boolean[] = [];
	let page = 1;
	let isLoading = false;
	let images: components['schemas']['ImageDto'][] = [];
	let showModal = false;
	let modalImg: components['schemas']['ImageDto'];
	let modaltags: string[] = [];
	let modalImgVote = true;
	let modalImgNum = 0;
	let modalImgIndex = 0;
	let comments = [];
	let comment = '';
	let sse: EventSource;
	let commentEditOpen = 0;
	let isPurchase = writable(false);
	let showPayModal = false;
	let showReportModal = false;
	let reportReason = '';
	let reportDetailReason = '';
	$: if (!showModal) {
		if (sse) {
			sse.close();
		}
	}
	function isLiked(image) {
		return image.voters.includes(rq.member.id);
	}
	function toggleLike(num, index) {
		switch (num) {
			case 1:
				images1Liked[index] = !images1Liked[index];
				break;
			case 2:
				images2Liked[index] = !images2Liked[index];
				break;
			case 3:
				images3Liked[index] = !images3Liked[index];
				break;
			case 4:
				images4Liked[index] = !images4Liked[index];
				break;
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
				date: newImages[i].date,
				price: newImages[i].price,
				downloadCount: newImages[i].downloadCount,
				voters: newImages[i].voters
			};

			switch (i % 4) {
				case 0:
					tempImages1.push(imageObject);
					images1Liked.push(isLiked(imageObject));
					break;
				case 1:
					tempImages2.push(imageObject);
					images2Liked.push(isLiked(imageObject));
					break;
				case 2:
					tempImages3.push(imageObject);
					images3Liked.push(isLiked(imageObject));
					break;
				case 3:
					tempImages4.push(imageObject);
					images4Liked.push(isLiked(imageObject));
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
	async function showModalFnc(img: components['schemas']['ImageDto'], num, index) {
		showModal = true;
		modalImg = img;
		modaltags = img.tags.split(',');
		modalImgNum = num;
		modalImgIndex = index;
		loadComments();
		sse = new EventSource(
			`${import.meta.env.VITE_CORE_API_BASE_URL}/api/sse/subscribe?imageId=${img.id}&memberId=${rq.member.id}`
		);
		sse.addEventListener('addComment', (e) => {
			const newComment = JSON.parse(e.data);
			comments = [newComment, ...comments];
		});
		const purchaseData = await rq.apiEndPoints().GET('/api/v1/purchase/isPurchase', {
			params: {
				query: {
					imageId: img.id
				}
			}
		});
		isPurchase.set(purchaseData.data?.data!);
		const { data, error } = await rq.apiEndPointsWithFetch(fetch).GET('/api/v1/image/vote/isvote', {
			params: {
				query: {
					imageId: img.id
				}
			}
		});
		if (data) {
			modalImgVote = data.data;
		}
	}
	async function saveComment() {
		if (rq.member.id == 0) {
			rq.msgError('로그인이 필요합니다');
			return;
		}
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
		if (rq.member.id == 0) {
			rq.msgError('로그인이 필요합니다');
			return;
		}
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
			toggleLike(modalImgNum, modalImgIndex);
		}
	}

	async function imagePuchase() {
		showPayModal = false;
		if (rq.member.id == 0) {
			rq.msgError('로그인이 필요합니다');
			return;
		}
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/purchase/imagepurchase', {
			params: {
				query: {
					imageId: modalImg.id
				}
			}
		});

		if (data?.success) {
			rq.msgInfo('구매가 완료되었습니다');
			isPurchase.set(true);
		} else {
			rq.msgError('포인트가 부족합니다');
		}
	}
	async function downloadImage() {
		const imageUrl = modalImg.path;
		const response = await fetch(imageUrl);
		const blob = await response.blob();
		const link = document.createElement('a');
		link.href = URL.createObjectURL(blob);
		link.download = 'downloaded_image.jpeg';
		link.click();
	}
	function showPayModalFnc() {
		showPayModal = true;
	}
	function showPayModalFncCan() {
		showPayModal = false;
	}
	function showModalFncCan() {
		showModal = false;
	}
	function showModalFncReport() {
		showReportModal = true;
	}
	async function report() {
		const reason = reportReason === '기타' ? reportDetailReason : reportReason;
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/report/save', {
			body: {
				imageId: modalImg.id,
				memberId: rq.member.id,
				reason: reason
			}
		});
		rq.msgInfo('신고 완료되었습니다');
		showReportModal = false;
	}
</script>

{#if isLoading}
	<p>Loading...</p>
{:else}
	<Modal bind:showModal>
		<div class="flex w-full h-4/5 mx-auto my-10 overflow-auto gap-20 max-h-[30rem]">
			{#if modalImg && modalImg.path}
				<div class="w-1/2">
					<img class="object-cover h-full w-full" src={modalImg.path} alt="" />
				</div>
				<div class="w-1/2 p-4 overflow-auto">
					<div class="flex justify-between items-center mb-4 gap-4">
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
									<span class="mx-1">{tag}</span>
								{/each}
							</div>
						</div>

						<div class="flex items-center justify-center gap-2">
							<button class="btn" on:click={showModalFncReport}
								><svg
									xmlns="http://www.w3.org/2000/svg"
									fill="none"
									viewBox="0 0 24 24"
									stroke-width="1.5"
									stroke="currentColor"
									class="size-6"
								>
									<path
										stroke-linecap="round"
										stroke-linejoin="round"
										d="M3 3v1.5M3 21v-6m0 0 2.77-.693a9 9 0 0 1 6.208.682l.108.054a9 9 0 0 0 6.086.71l3.114-.732a48.524 48.524 0 0 1-.005-10.499l-3.11.732a9 9 0 0 1-6.085-.711l-.108-.054a9 9 0 0 0-6.208-.682L3 4.5M3 15V4.5"
									/>
								</svg>
							</button>
							{#if $isPurchase}
								<button class="btn" on:click={downloadImage}>다운로드</button>
							{:else}
								<button class="btn" on:click={showPayModalFnc}>구매하기</button>
							{/if}
							<button class="btn mr-2" on:click={voteImage}>
								{#if !modalImgVote}
									<svg class="w-6 h-6 text-red-500" fill="currentColor" viewBox="0 0 24 24">
										<path
											d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
											fill="none"
											stroke="currentColor"
											stroke-width="2"
										></path>
									</svg>
								{:else}
									<svg class="w-6 h-6 text-red-500" fill="currentColor" viewBox="0 0 24 24">
										<path
											d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
										></path>
									</svg>
								{/if}
							</button>
						</div>
					</div>
					<div class="author text-lg font-bold justify-between flex">
						<span>Author: {modalImg.member_nickname}</span><span>{modalImg.price} Point</span>
					</div>
					<div class="date text-sm text-gray-500">
						Date: {new Date(modalImg.date).toLocaleDateString('ko-KR')}
					</div>
					<div class="description mt-2 text-gray-700">
						{modalImg.content}
					</div>
					<div class="comments mt-4">
						<h3 class="text-lg font-bold">Comments</h3>
						<form class=" gap-2 mt-2 flex items-center justify-center" on:submit={saveComment}>
							<input
								bind:value={comment}
								class="input input-bordered w-full"
								type="text"
								placeholder="Add a comment..."
							/>
							<button class="btn" type="submit">댓글 쓰기</button>
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
									<button class="btn btn-outline hover:bg-gray-100 hover:text-black flex-col h-14">
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
		<div class="flex justify-end">
			<button on:click={showModalFncCan} class="text-gray-500 hover:text-gray-800">닫기</button>
		</div>
	</Modal>
	<Modal bind:showModal={showPayModal}>
		{#if modalImg && modalImg.path}
			<div class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center">
				<div class="bg-white rounded-lg shadow-lg p-6 w-80">
					<div class="flex justify-between items-center mb-4">
						<h2 class="text-xl font-bold">이미지 구매</h2>
					</div>
					<p class="mb-4">
						현재 잔액은 {rq.member.cache}원 입니다. 구매 가격은{modalImg.price}원 입니다.
					</p>
					<div class="flex justify-between mb-6">
						<div>
							<p class="font-medium">현재 잔액</p>
							<p class="text-xl font-bold">{rq.member.cache} 원</p>
						</div>
						<div>
							<p class="font-medium">구매 가격</p>
							<p class="text-xl font-bold">{modalImg.price} 원</p>
						</div>
					</div>
					<div class="flex justify-end space-x-4">
						<button
							on:click={showPayModalFncCan}
							class="bg-gray-200 text-gray-700 px-4 py-2 rounded hover:bg-gray-300">취소</button
						>

						<button
							on:click={imagePuchase}
							class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">구매하기</button
						>
					</div>
				</div>
			</div>
		{/if}
	</Modal>
	<Modal bind:showModal={showReportModal}>
		{#if modalImg && modalImg.path}
			<div class="p-6">
				<h2 class="text-xl font-bold mb-4">이미지 신고</h2>
				<form on:submit|preventDefault={report}>
					<div class="mb-4">
						<label class="block mb-2">
							<input
								type="radio"
								name="reason"
								value="홍보성 이미지입니다"
								bind:group={reportReason}
								class="mr-2"
							/>
							홍보성 이미지입니다
						</label>
						<label class="block mb-2">
							<input
								type="radio"
								name="reason"
								value="선정적 이미지입니다"
								bind:group={reportReason}
								class="mr-2"
							/>
							선정적 이미지입니다
						</label>
						<label class="block mb-2">
							<input
								type="radio"
								name="reason"
								value="기타"
								bind:group={reportReason}
								class="mr-2"
							/>
							기타
						</label>
						<textarea
							bind:value={reportDetailReason}
							class="w-full mt-2 p-2 border border-gray-300 rounded"
							placeholder="기타 사유를 입력하세요..."
							disabled={reportReason !== '기타'}
						></textarea>
					</div>
					<div class="flex justify-end">
						<button
							type="button"
							class="mr-2 bg-gray-300 text-gray-700 px-4 py-2 rounded"
							on:click={() => (showReportModal = false)}>취소</button
						>
						<button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">신고</button>
					</div>
				</form>
			</div>
		{/if}
	</Modal>
	<div class="container mx-auto items-center mt-20">
		<div class="grid grid-cols-2 md:grid-cols-4 gap-4">
			<div class="grid gap-4">
				{#each images1 as image, index}
					<div class="image-container">
						<img
							on:click={() => showModalFnc(image, 1, index)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
						{#if images1Liked[index]}
							<div class="like-icon">
								<svg class="w-6 h-6 text-red-500" fill="currentColor" viewBox="0 0 24 24">
									<path
										d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
									></path>
								</svg>
							</div>
						{/if}
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images2 as image, index}
					<div class="image-container">
						<img
							on:click={() => showModalFnc(image, 2, index)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
						{#if images2Liked[index]}
							<div class="like-icon">
								<svg class="w-6 h-6 text-red-500" fill="currentColor" viewBox="0 0 24 24">
									<path
										d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
									></path>
								</svg>
							</div>
						{/if}
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images3 as image, index}
					<div class="image-container">
						<img
							on:click={() => showModalFnc(image, 3, index)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
						{#if images3Liked[index]}
							<div class="like-icon">
								<svg class="w-6 h-6 text-red-500" fill="currentColor" viewBox="0 0 24 24">
									<path
										d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
									></path>
								</svg>
							</div>
						{/if}
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images4 as image, index}
					<div class="image-container">
						<img
							on:click={() => showModalFnc(image, 4, index)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
						{#if images4Liked[index]}
							<div class="like-icon">
								<svg class="w-6 h-6 text-red-500" fill="currentColor" viewBox="0 0 24 24">
									<path
										d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
									></path>
								</svg>
							</div>
						{/if}
					</div>
				{/each}
			</div>
		</div>
	</div>
{/if}

<style>
	.image-container {
		position: relative;
	}
	.like-icon {
		position: absolute;
		top: 8px;
		right: 8px;
		background: rgba(255, 255, 255, 0.7);
		border-radius: 50%;
		padding: 4px;
	}
</style>
