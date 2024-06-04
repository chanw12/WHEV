<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';
	import { onMount } from 'svelte';

	// let images: components['schemas']['ImageDto'][] = [];
	let isLoading = false;
	async function loadImages() {
		isLoading = false;
	}
	onMount(() => {
		loadImages();
	});

	// Sample data
	const images = [
		{
			id: 1,
			src: import.meta.env.VITE_CORE_API_BASE_URL + '/gen/1.jpg',
			liked: true
		},
		{
			id: 2,
			src: import.meta.env.VITE_CORE_API_BASE_URL + '/gen/2.jpg',
			liked: false
		},
		{
			id: 3,
			src: import.meta.env.VITE_CORE_API_BASE_URL + '/gen/3.jpg',
			liked: true
		}
		// Add more image objects here...
	];
</script>

{#if isLoading}
	<p>Loading...</p>
{:else}
	<div class="container mx-auto p-4 mt-20">
		<h1 class="text-xl font-bold mb-4">Liked Images</h1>
		<div class="image-grid">
			{#each images as image (image.id)}
				<div class="image-container">
					<img src={image.src} alt="Image" class="w-full h-auto rounded-lg shadow-lg" />
					{#if image.liked}
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
{/if}

<style>
	.image-grid {
		display: grid;
		grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
		gap: 1rem;
	}
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
