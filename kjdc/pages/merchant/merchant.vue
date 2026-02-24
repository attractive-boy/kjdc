<template>
	<view class="merchant-container">
		<view class="header">
			<view class="title">商家中心</view>
			<view class="sub">店铺ID：{{ shopId }}</view>
		</view>

		<view class="section">
			<view class="section-title">店铺信息</view>
			<view class="card">
				<view class="row">
					<text class="label">店铺名称</text>
					<input class="input" v-model="shopForm.name" placeholder="请输入店铺名称" />
				</view>
				<view class="row">
					<text class="label">联系电话</text>
					<input class="input" v-model="shopForm.phone" placeholder="请输入联系电话" />
				</view>
				<view class="row">
					<text class="label">店铺地址</text>
					<input class="input" v-model="shopForm.address" placeholder="请输入店铺地址" />
				</view>
				<view class="row">
					<text class="label">营业时间</text>
					<input class="input" v-model="shopForm.businessHours" placeholder="10:00-22:00" />
				</view>
				<view class="row">
					<text class="label">营业状态</text>
					<switch :checked="shopForm.open" @change="toggleOpen" />
				</view>
				<button class="btn-primary" @click="saveShop">保存店铺信息</button>
			</view>
		</view>

		<view class="section">
			<view class="section-title">菜品管理</view>
			<view class="card">
				<view class="row">
					<text class="label">菜品名称</text>
					<input class="input" v-model="newDish.name" placeholder="例如：宫保鸡丁" />
				</view>
				<view class="row">
					<text class="label">价格</text>
					<input class="input" type="number" v-model="newDish.price" placeholder="例如：38" />
				</view>
				<view class="row">
					<text class="label">分类</text>
					<input class="input" v-model="newDish.category" placeholder="热菜/凉菜/主食" />
				</view>
				<view class="row">
					<text class="label">图片URL</text>
					<input class="input" v-model="newDish.imageUrl" placeholder="https://..." />
				</view>
				<button class="btn-primary" @click="createDish">新增菜品</button>
			</view>

			<view v-if="dishes.length" class="list">
				<view v-for="dish in dishes" :key="dish.id" class="list-item">
					<view class="info">
						<view class="name">{{ dish.name }}</view>
						<view class="meta">¥{{ dish.price }} · {{ dish.category }}</view>
					</view>
					<button class="btn-text" @click="deleteDish(dish.id)">删除</button>
				</view>
			</view>
			<view v-else class="empty">暂无菜品</view>
		</view>

		<view class="section">
			<view class="section-title">订单列表</view>
			<view v-if="orders.length" class="list">
				<view v-for="order in orders" :key="order.id" class="list-item">
					<view class="info">
						<view class="name">{{ order.orderNo }}</view>
						<view class="meta">{{ order.status }} · ¥{{ order.totalAmount }}</view>
					</view>
				</view>
			</view>
			<view v-else class="empty">暂无订单</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				apiUrl: 'http://localhost:8080/api',
				shopId: 1,
				shopForm: {
					name: '',
					phone: '',
					address: '',
					businessHours: '',
					open: true
				},
				newDish: {
					name: '',
					price: '',
					category: '',
					imageUrl: ''
				},
				dishes: [],
				orders: []
			}
		},
		onLoad(options) {
			if (options.shopId) {
				this.shopId = Number(options.shopId)
				uni.setStorageSync('merchantShopId', this.shopId)
			} else {
				const cached = uni.getStorageSync('merchantShopId')
				if (cached) {
					this.shopId = Number(cached)
				}
			}
			this.loadShop()
			this.loadDishes()
			this.loadOrders()
		},
		methods: {
			loadShop() {
				uni.request({
					url: `${this.apiUrl}/merchant/shops/${this.shopId}`,
					method: 'GET',
					success: (res) => {
						if (res.data && res.data.data) {
							const shop = res.data.data
							this.shopForm = {
								name: shop.name || '',
								phone: shop.phone || '',
								address: shop.address || '',
								businessHours: shop.businessHours || '',
								open: !!shop.open
							}
						}
					}
				})
			},
			saveShop() {
				uni.request({
					url: `${this.apiUrl}/merchant/shops/${this.shopId}`,
					method: 'PUT',
					data: this.shopForm,
					success: () => {
						uni.showToast({ title: '保存成功', icon: 'success' })
					}
				})
			},
			toggleOpen(e) {
				const open = e.detail.value
				this.shopForm.open = open
				uni.request({
					url: `${this.apiUrl}/merchant/shops/${this.shopId}/open?open=${open}`,
					method: 'PUT'
				})
			},
			loadDishes() {
				uni.request({
					url: `${this.apiUrl}/merchant/shops/${this.shopId}/dishes`,
					method: 'GET',
					success: (res) => {
						this.dishes = res.data && res.data.data ? res.data.data : []
					}
				})
			},
			createDish() {
				if (!this.newDish.name || !this.newDish.price) {
					uni.showToast({ title: '请输入名称和价格', icon: 'none' })
					return
				}
				uni.request({
					url: `${this.apiUrl}/merchant/shops/${this.shopId}/dishes`,
					method: 'POST',
					data: {
						name: this.newDish.name,
						price: Number(this.newDish.price),
						category: this.newDish.category,
						imageUrl: this.newDish.imageUrl
					},
					success: () => {
						uni.showToast({ title: '新增成功', icon: 'success' })
						this.newDish = { name: '', price: '', category: '', imageUrl: '' }
						this.loadDishes()
					}
				})
			},
			deleteDish(id) {
				uni.showModal({
					title: '确认删除',
					content: '确定删除该菜品吗？',
					success: (res) => {
						if (res.confirm) {
							uni.request({
								url: `${this.apiUrl}/merchant/dishes/${id}`,
								method: 'DELETE',
								success: () => {
									uni.showToast({ title: '已删除', icon: 'success' })
									this.loadDishes()
								}
							})
						}
					}
				})
			},
			loadOrders() {
				uni.request({
					url: `${this.apiUrl}/merchant/shops/${this.shopId}/orders`,
					method: 'GET',
					success: (res) => {
						this.orders = res.data && res.data.data ? res.data.data : []
					}
				})
			}
		}
	}
</script>

<style scoped>
	.merchant-container {
		padding: 20rpx;
		background: #f5f5f5;
		min-height: 100vh;
	}

	.header {
		background: white;
		border-radius: 16rpx;
		padding: 24rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
	}

	.title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.sub {
		font-size: 24rpx;
		color: #999;
		margin-top: 8rpx;
	}

	.section {
		margin-top: 20rpx;
	}

	.section-title {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 12rpx;
	}

	.card {
		background: white;
		border-radius: 12rpx;
		padding: 20rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
	}

	.row {
		display: flex;
		align-items: center;
		margin-bottom: 16rpx;
	}

	.label {
		width: 160rpx;
		font-size: 24rpx;
		color: #666;
	}

	.input {
		flex: 1;
		background: #f7f7f7;
		border-radius: 8rpx;
		padding: 12rpx;
		font-size: 24rpx;
	}

	.list {
		background: white;
		border-radius: 12rpx;
		padding: 12rpx;
	}

	.list-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 12rpx;
		border-bottom: 1rpx solid #eee;
	}

	.list-item:last-child {
		border-bottom: none;
	}

	.info .name {
		font-size: 26rpx;
		color: #333;
	}

	.info .meta {
		font-size: 22rpx;
		color: #999;
	}

	.btn-text {
		background: none;
		color: #ff4d4f;
		font-size: 24rpx;
	}

	.empty {
		text-align: center;
		color: #999;
		padding: 24rpx 0;
	}
</style>
