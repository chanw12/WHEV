<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';
	import { onMount } from 'svelte';

	let images: components['schemas']['ImageDto'][] = [];
	let isLoading = false;
	async function loadImages() {
		isLoading = true;
		const response = await rq.apiEndPoints().GET(`/api/v1/image/{memberId}`, {
			params: {
				path: {
					memberId: rq.member.id
				}
			}
		});
		images = response.data?.data.items;
	}
	onMount(() => {
		loadImages();
	});
</script>

{#if isLoading}
	<p>Loading...</p>
{:else}
	{#each image as images}{/each}
{/if}
