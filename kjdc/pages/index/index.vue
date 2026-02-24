<template>
	<view class="container">
		<!-- 顶部搜索和随机按钮 -->
		<view class="header">
			<view class="search-bar">
				<input 
					type="text" 
					placeholder="搜索菜品..."
					v-model="searchKeyword"
					@confirm="searchDishes"
					class="search-input"
				/>
				<text class="icon-search">🔍</text>
			</view>
			<button class="btn-random" @click="getRandomDish">🎲 随机推荐</button>
		</view>

		<!-- 分类标签 -->
		<scroll-view class="category-scroll" scroll-x="true">
			<view class="category-tags">
				<view 
					v-for="(cat, idx) in categories"
					:key="idx"
					class="tag"
					:class="{ 'tag-active': selectedCategory === cat }"
					@click="selectCategory(cat)"
				>
					{{ cat || '全部' }}
				</view>
			</view>
		</scroll-view>

		<!-- 菜品列表 -->
		<scroll-view class="dish-list" scroll-y="true" @scrolltolower="loadMoreDishes">
			<!-- 随机推荐弹窗 -->
			<view v-if="randomDish" class="random-dish-card">
				<view class="random-title">🎉 今日推荐</view>
				<view class="random-content">
					<image :src="randomDish.imageUrl" class="random-image"></image>
					<view class="random-info">
						<view class="random-name">{{ randomDish.name }}</view>
						<view class="random-desc">{{ randomDish.description }}</view>
						<view class="random-footer">
							<text class="price">¥{{ randomDish.price }}</text>
							<button class="btn-add" @click="addToCartRandom">加入购物车</button>
						</view>
					</view>
				</view>
				<view class="close-btn" @click="randomDish = null">✕</view>
			</view>

			<!-- 菜品卡片网格 -->
			<view v-if="dishes.length > 0" class="dishes-grid">
				<view 
					v-for="dish in dishes" 
					:key="dish.id"
					class="dish-card"
					@click="goToDishDetail(dish)"
				>
					<image :src="dish.imageUrl" class="dish-image"></image>
					<view class="dish-info">
						<view class="dish-name">{{ dish.name }}</view>
						<view class="dish-desc">{{ dish.description.substring(0, 20) }}...</view>
						<view class="dish-footer">
							<text class="dish-price">¥{{ dish.price }}</text>
							<view class="dish-rating">⭐ {{ dish.rating || '4.5' }}</view>
						</view>
						<button class="btn-add-dish" @click.stop="addToCart(dish)">+</button>
					</view>
				</view>
			</view>

			<!-- 空状态 -->
			<view v-else class="empty-state">
				<text class="empty-icon">🍽</text>
				<text>暂无菜品</text>
			</view>

			<!-- 加载提示 -->
			<view v-if="isLoading" class="loading">
				<text>加载中...</text>
			</view>
		</scroll-view>

		<!-- 浮动购物车按钮 -->
		<view class="cart-fab" @click="goToCart" :class="{ 'cart-active': cartCount > 0 }">
			<text class="cart-icon">🛒</text>
			<view v-if="cartCount > 0" class="cart-badge">{{ cartCount }}</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				dishes: [],
				categories: ['全部'],
				selectedCategory: '全部',
				searchKeyword: '',
				randomDish: null,
				cartCount: 0,
				isLoading: false,
				apiUrl: 'http://localhost:8080/api'
			}
		},
		onLoad() {
			this.loadCategories()
			this.loadDishes()
			this.loadCartCount()
		},
		onShow() {
			this.loadCartCount()
		},
		methods: {
			// 加载菜品分类
			loadCategories() {
				uni.request({
					url: `${this.apiUrl}/dishes/categories`,
					method: 'GET',
					success: (res) => {
						if (res.data.code === 200) {
							this.categories = ['全部', ...res.data.data]
						}
					},
					fail: () => {
						uni.showToast({ title: '加载分类失败', icon: 'error' })
					}
				})
			},

			// 加载菜品列表
			loadDishes() {
				this.isLoading = true
				let url = `${this.apiUrl}/dishes`
				
				if (this.searchKeyword) {
					url = `${this.apiUrl}/dishes/search?keyword=${this.searchKeyword}`
				} else if (this.selectedCategory && this.selectedCategory !== '全部') {
					url = `${this.apiUrl}/dishes/category/${this.selectedCategory}`
				}

				uni.request({
					url: url,
					method: 'GET',
					success: (res) => {
						if (res.data.code === 200) {
							this.dishes = res.data.data || []
						}
					},
					fail: () => {
						uni.showToast({ title: '加载菜品失败', icon: 'error' })
					},
					complete: () => {
						this.isLoading = false
					}
				})
			},

			// 选择分类
			selectCategory(category) {
				this.selectedCategory = category
				this.loadDishes()
			},

			// 搜索菜品
			searchDishes() {
				this.loadDishes()
			},

			// 随机推荐
			getRandomDish() {
				uni.request({
					url: `${this.apiUrl}/dishes/random`,
					method: 'GET',
					success: (res) => {
						if (res.data.code === 200) {
							this.randomDish = res.data.data
						} else {
							uni.showToast({ title: '暂无可推荐菜品', icon: 'none' })
						}
					},
					fail: () => {
						uni.showToast({ title: '获取推荐失败', icon: 'error' })
					}
				})
			},

			// 加入购物车
			addToCart(dish) {
				uni.getStorage({
					key: 'cart',
					success: (res) => {
						let cart = res.data || []
						let item = cart.find(d => d.id === dish.id)
						if (item) {
							item.quantity++
						} else {
							cart.push({ ...dish, quantity: 1 })
						}
						uni.setStorage({
							key: 'cart',
							data: cart,
							success: () => {
								this.loadCartCount()
								uni.showToast({ title: '已加入购物车', icon: 'success' })
							}
						})
					},
					fail: () => {
						let cart = [{ ...dish, quantity: 1 }]
						uni.setStorage({
							key: 'cart',
							data: cart,
							success: () => {
								this.loadCartCount()
								uni.showToast({ title: '已加入购物车', icon: 'success' })
							}
						})
					}
				})
			},

			// 快速推荐加入购物车
			addToCartRandom() {
				if (this.randomDish) {
					this.addToCart({
						id: this.randomDish.id,
						name: this.randomDish.name,
						description: this.randomDish.description,
						price: this.randomDish.price,
						imageUrl: this.randomDish.imageUrl,
						rating: this.randomDish.rating
					})
					this.randomDish = null
				}
			},

			// 加载购物车数量
			loadCartCount() {
				uni.getStorage({
					key: 'cart',
					success: (res) => {
						let cart = res.data || []
						this.cartCount = cart.reduce((sum, item) => sum + item.quantity, 0)
					},
					fail: () => {
						this.cartCount = 0
					}
				})
			},

			// 进入菜品详情页
			goToDishDetail(dish) {
				uni.navigateTo({
					url: `/pages/dish-detail/dish-detail?id=${dish.id}`
				})
			},

			// 进入购物车
			goToCart() {
				uni.navigateTo({
					url: '/pages/cart/cart'
				})
			},

			// 加载更多（下拉刷新）
			loadMoreDishes() {
				this.loadDishes()
			}
		}
	}
</script>

<style scoped>
	.container {
		display: flex;
		flex-direction: column;
		height: 100vh;
		background-color: #f5f5f5;
	}

	.header {
		background: white;
		padding: 20rpx;
		display: flex;
		gap: 12rpx;
		align-items: center;
		box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.05);
	}

	.search-bar {
		flex: 1;
		display: flex;
		align-items: center;
		background: #f0f0f0;
		border-radius: 20rpx;
		padding: 10rpx 15rpx;
		position: relative;
	}

	.search-input {
		flex: 1;
		border: none;
		background: transparent;
		font-size: 26rpx;
		outline: none;
	}

	.icon-search {
		font-size: 24rpx;
		color: #999;
	}

	.btn-random {
		background: #667eea;
		color: white;
		border: none;
		border-radius: 20rpx;
		padding: 10rpx 20rpx;
		font-size: 24rpx;
		min-width: 120rpx;
		text-align: center;
	}

	.category-scroll {
		white-space: nowrap;
		background: white;
		border-bottom: 1rpx solid #eee;
	}

	.category-tags {
		display: inline-flex;
		padding: 15rpx 20rpx;
		gap: 12rpx;
	}

	.tag {
		background: #f0f0f0;
		color: #666;
		padding: 8rpx 20rpx;
		border-radius: 20rpx;
		font-size: 26rpx;
		white-space: nowrap;
	}

	.tag-active {
		background: #667eea;
		color: white;
	}

	.dish-list {
		flex: 1;
		overflow-y: auto;
	}

	.random-dish-card {
		background: white;
		margin: 20rpx;
		border-radius: 12rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
		position: relative;
		overflow: hidden;
	}

	.random-title {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: white;
		padding: 12rpx 20rpx;
		font-weight: bold;
		font-size: 28rpx;
	}

	.random-content {
		display: flex;
		padding: 20rpx;
		gap: 20rpx;
	}

	.random-image {
		width: 150rpx;
		height: 150rpx;
		border-radius: 8rpx;
		object-fit: cover;
	}

	.random-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.random-name {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
	}

	.random-desc {
		font-size: 24rpx;
		color: #999;
		margin-top: 8rpx;
	}

	.random-footer {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-top: 12rpx;
	}

	.price {
		font-size: 32rpx;
		color: #e74c3c;
		font-weight: bold;
	}

	.btn-add {
		background: #667eea;
		color: white;
		border: none;
		border-radius: 6rpx;
		padding: 8rpx 16rpx;
		font-size: 24rpx;
		min-width: 80rpx;
	}

	.close-btn {
		position: absolute;
		top: 10rpx;
		right: 15rpx;
		font-size: 28rpx;
		color: #999;
		cursor: pointer;
	}

	.dishes-grid {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 16rpx;
		padding: 16rpx;
	}

	.dish-card {
		background: white;
		border-radius: 12rpx;
		overflow: hidden;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
		position: relative;
	}

	.dish-image {
		width: 100%;
		height: 180rpx;
		object-fit: cover;
	}

	.dish-info {
		padding: 12rpx;
	}

	.dish-name {
		font-size: 26rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 4rpx;
	}

	.dish-desc {
		font-size: 22rpx;
		color: #999;
		margin-bottom: 8rpx;
	}

	.dish-footer {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-top: 8rpx;
	}

	.dish-price {
		font-size: 28rpx;
		color: #e74c3c;
		font-weight: bold;
	}

	.dish-rating {
		font-size: 22rpx;
		color: #f39c12;
	}

	.btn-add-dish {
		position: absolute;
		bottom: 12rpx;
		right: 12rpx;
		width: 50rpx;
		height: 50rpx;
		border-radius: 50%;
		background: #667eea;
		color: white;
		border: none;
		font-size: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 0;
	}

	.cart-fab {
		position: fixed;
		bottom: 30rpx;
		right: 30rpx;
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background: white;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 40rpx;
		z-index: 100;
	}

	.cart-active {
		background: #667eea;
	}

	.cart-icon {
		display: block;
	}

	.cart-badge {
		position: absolute;
		top: -5rpx;
		right: -5rpx;
		min-width: 30rpx;
		height: 30rpx;
		border-radius: 50%;
		background: #e74c3c;
		color: white;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 22rpx;
		font-weight: bold;
	}

	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 100rpx 30rpx;
		color: #999;
	}

	.empty-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
	}

	.loading {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 50rpx;
	}
</style>
