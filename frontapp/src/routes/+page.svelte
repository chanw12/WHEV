<script>
	import { onMount } from 'svelte';
	import axios from 'axios';
	import Modal from '$lib/components/Modal.svelte';

	let images1 = [];
	let images2 = [];
	let images3 = [];
	let images4 = [];
	let page = 1;
	let isLoading = false;
	let images = [];
	let showModal = false;
	let ModalImgSrc = '';

	async function loadImages() {
		let tempImages1 = [...images1];
		let tempImages2 = [...images2];
		let tempImages3 = [...images3];
		let tempImages4 = [...images4];
		const response = await axios.get(
			import.meta.env.VITE_CORE_API_BASE_URL + '/api/v1/image?page=' + page
		);
		const newImages = response.data.data.items.content;
		if (newImages.length == 0) {
			isLoading = false;
			return;
		}
		for (let i = 0; i < newImages.length; i++) {
			const imageObject = {
				alt: newImages[i].content,
				id: newImages[i].id,
				member_id: newImages[i].member_id,
				src: import.meta.env.VITE_CORE_API_BASE_URL + '/gen' + newImages[i].path,
				tags: newImages[i].tags
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
		console.log(newImages);

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
	function showModalFnc(imgsrc) {
		console.log('chan');
		showModal = true;
		ModalImgSrc = imgsrc;
	}
</script>

{#if isLoading}
	<p>Loading...</p>
{:else}
	<Modal bind:showModal>
		<h2 slot="header">
			modal
			<small><em>adjective</em> mod·al \ˈmō-dəl\</small>
		</h2>

		<ol class="definition-list">
			<li>of or relating to modality in logic</li>
			<li>
				containing provisions as to the mode of procedure or the manner of taking effect —used of a
				contract or legacy
			</li>
			<li>of or relating to a musical mode</li>
			<li>of or relating to structure as opposed to substance</li>
			<li>
				of, relating to, or constituting a grammatical form or category characteristically
				indicating predication
			</li>
			<li>of or relating to a statistical mode</li>
			<img src={ModalImgSrc} alt="" />
		</ol>

		<a href="https://www.merriam-webster.com/dictionary/modal">merriam-webster.com</a>
	</Modal>

	<div class="container mx-auto items-center mt-20">
		<div class="grid grid-cols-2 md:grid-cols-4 gap-4">
			<div class="grid gap-4">
				{#each images1 as image}
					<div>
						<img
							on:click={() => showModalFnc(image.src)}
							class="h-auto max-w-full rounded-lg"
							src={image.src}
							alt={image.alt}
						/>
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images2 as image}
					<div>
						<img class="h-auto max-w-full rounded-lg" src={image.src} alt={image.alt} />
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images3 as image}
					<div>
						<img class="h-auto max-w-full rounded-lg" src={image.src} alt={image.alt} />
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images1 as image}
					<div>
						<img class="h-auto max-w-full rounded-lg" src={image.src} alt={image.alt} />
					</div>
				{/each}
			</div>
		</div>
	</div>
{/if}
