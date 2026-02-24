<template>
	<view class="admin-container">
		<view class="header">
			<view class="title">管理员控制台</view>
			<view class="sub">店铺审核与用户管理</view>
		</view>

		<view class="section">
			<view class="section-title">店铺审核</view>
			<view v-if="shops.length" class="list">
				<view v-for="shop in shops" :key="shop.id" class="list-item">
					<view class="info">
						<view class="name">{{ shop.name }}</view>
						<view class="meta">{{ shop.address || '未填写地址' }}</view>
					</view>
					<view class="actions">
						<text class="tag" :class="shop.approved ? 'tag-ok' : 'tag-warn'">
							{{ shop.approved ? '已审核' : '待审核' }}
						</text>
						<button class="btn-text" @click="toggleApprove(shop)">
							{{ shop.approved ? '取消' : '通过' }}
						</button>
					</view>
				</view>
			</view>
			<view v-else class="empty">暂无店铺</view>
		</view>

		<view class="section">
			<view class="section-title">用户列表</view>
			<view v-if="users.length" class="list">
				<view v-for="user in users" :key="user.id" class="list-item">
					<view class="info">
						<view class="name">{{ user.nickname || user.username }}</view>
						<view class="meta">{{ user.phone || '未绑定手机' }}</view>
					</view>
				</view>
			</view>
			<view v-else class="empty">暂无用户</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				apiUrl: 'http://localhost:8080/api',
				shops: [],
				users: []
			}
		},
		onLoad() {
			this.loadShops()
			this.loadUsers()
		},
		methods: {
			loadShops() {
				uni.request({
					url: `${this.apiUrl}/admin/shops`,
					method: 'GET',
					success: (res) => {
						this.shops = res.data && res.data.data ? res.data.data : []
					}
				})
			},
			loadUsers() {
				uni.request({
					url: `${this.apiUrl}/admin/users`,
					method: 'GET',
					success: (res) => {
						this.users = res.data && res.data.data ? res.data.data : []
					}
				})
			},
			toggleApprove(shop) {
				const target = !shop.approved
				uni.request({
					url: `${this.apiUrl}/admin/shops/${shop.id}/approve?approved=${target}`,
					method: 'PUT',
					success: () => {
						shop.approved = target
						uni.showToast({ title: '已更新', icon: 'success' })
					}
				})
			}
		}
	}
</script>

<style scoped>
	.admin-container {
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

	.actions {
		display: flex;
		align-items: center;
		gap: 12rpx;
	}

	.tag {
		padding: 4rpx 12rpx;
		border-radius: 12rpx;
		font-size: 20rpx;
	}

	.tag-ok {
		background: #e6fffb;
		color: #13c2c2;
	}

	.tag-warn {
		background: #fff7e6;
		color: #fa8c16;
	}

	.btn-text {
		background: none;
		color: #1677ff;
		font-size: 24rpx;
	}

	.empty {
		text-align: center;
		color: #999;
		padding: 24rpx 0;
	}
</style>
