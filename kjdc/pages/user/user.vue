<template>
	<view class="user-container">
		<!-- 用户信息卡片 -->
		<view class="user-card">
			<view class="user-info">
				<image v-if="user.avatarUrl" :src="user.avatarUrl" class="avatar"></image>
				<view v-else class="avatar-default">👤</view>
				<view class="user-details">
					<view class="user-name">{{ user.nickname || '未登录' }}</view>
					<view class="user-phone">{{ user.phone || '点击添加手机号' }}</view>
				</view>
			</view>
			<button class="btn-edit" @click="editUserInfo">编辑</button>
		</view>

		<!-- 功能菜单 -->
		<view class="menu-section">
			<!-- 订单管理 -->
			<view class="menu-item" @click="goToOrderList">
				<text class="menu-icon">📋</text>
				<text class="menu-label">我的订单</text>
				<text class="menu-arrow">›</text>
			</view>

			<!-- 收藏 -->
			<view class="menu-item" @click="goToCollections">
				<text class="menu-icon">❤️</text>
				<text class="menu-label">我的收藏</text>
				<text class="menu-arrow">›</text>
			</view>

			<!-- 地址管理 -->
			<view class="menu-item" @click="goToAddresses">
				<text class="menu-icon">📍</text>
				<text class="menu-label">收货地址</text>
				<text class="menu-arrow">›</text>
			</view>

			<!-- 优惠券 -->
			<view class="menu-item" @click="goToCoupons">
				<text class="menu-icon">🎟️</text>
				<text class="menu-label">我的优惠券</text>
				<text class="menu-arrow">›</text>
			</view>

			<!-- 商家中心 -->
			<view class="menu-item" @click="goToMerchantCenter">
				<text class="menu-icon">🏪</text>
				<text class="menu-label">商家中心</text>
				<text class="menu-arrow">›</text>
			</view>

			<!-- 管理员控制台 -->
			<view class="menu-item" @click="goToAdminCenter">
				<text class="menu-icon">🛡️</text>
				<text class="menu-label">管理员控制台</text>
				<text class="menu-arrow">›</text>
			</view>
		</view>

		<!-- 设置菜单 -->
		<view class="menu-section" style="margin-top: 30rpx;">
			<!-- 关于 -->
			<view class="menu-item" @click="showAbout">
				<text class="menu-icon">ℹ️</text>
				<text class="menu-label">关于快餐点餐</text>
				<text class="menu-arrow">›</text>
			</view>

			<!-- 联系客服 -->
			<view class="menu-item" @click="contactService">
				<text class="menu-icon">💬</text>
				<text class="menu-label">联系客服</text>
				<text class="menu-arrow">›</text>
			</view>

			<!-- 意见反馈 -->
			<view class="menu-item" @click="goToFeedback">
				<text class="menu-icon">📝</text>
				<text class="menu-label">意见反馈</text>
				<text class="menu-arrow">›</text>
			</view>
		</view>

		<!-- 登出按钮 -->
		<view class="logout-section" v-if="user.nickname">
			<button class="btn-logout" @click="logout">登出</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				user: {
					id: 1,
					nickname: '用户昵称',
					phone: '',
					avatarUrl: '',
					defaultAddress: ''
				}
			}
		},
		onLoad() {
			this.loadUserInfo()
		},
		onShow() {
			this.loadUserInfo()
		},
		methods: {
			loadUserInfo() {
				uni.getStorage({
					key: 'userInfo',
					success: (res) => {
						if (res.data.nickName) {
							this.user.nickname = res.data.nickName
							this.user.avatarUrl = res.data.avatarUrl
						}
					},
					fail: () => {
						console.log('用户未授权')
					}
				})
			},

			editUserInfo() {
				uni.showModal({
					title: '编辑个人信息',
					content: '功能开发中...',
					showCancel: false
				})
			},

			goToOrderList() {
				uni.navigateTo({
					url: '/pages/order-list/order-list'
				})
			},

			goToCollections() {
				uni.showToast({ title: '功能开发中...', icon: 'none' })
			},

			goToAddresses() {
				uni.showToast({ title: '功能开发中...', icon: 'none' })
			},

			goToCoupons() {
				uni.showToast({ title: '功能开发中...', icon: 'none' })
			},

			goToMerchantCenter() {
				uni.navigateTo({
					url: '/pages/merchant/merchant'
				})
			},

			goToAdminCenter() {
				uni.navigateTo({
					url: '/pages/admin/admin'
				})
			},

			showAbout() {
				uni.showModal({
					title: '关于快捷点餐系统',
					content: '快捷点餐系统v1.0.0\n让点餐更简单，让选择不再困难！',
					showCancel: false
				})
			},

			contactService() {
				uni.makePhoneCall({
					phoneNumber: '400-800-8888',
					success: () => {
						console.log('拨打成功')
					}
				})
			},

			goToFeedback() {
				uni.navigateTo({
					url: '/pages/feedback/feedback'
				})
			},

			logout() {
				uni.showModal({
					title: '确认登出',
					content: '确定要退出登录吗？',
					success: (res) => {
						if (res.confirm) {
							uni.clearStorage()
							uni.showToast({ title: '已登出', icon: 'success' })
							uni.reLaunch({
								url: '/pages/index/index'
							})
						}
					}
				})
			}
		}
	}
</script>

<style scoped>
	.user-container {
		display: flex;
		flex-direction: column;
		height: 100vh;
		background-color: #f5f5f5;
		padding-top: 20rpx;
	}

	.user-card {
		background: white;
		border-radius: 12rpx;
		padding: 20rpx;
		margin: 0 20rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
		margin-bottom: 20rpx;
	}

	.user-info {
		display: flex;
		align-items: center;
		gap: 16rpx;
		flex: 1;
	}

	.avatar,
	.avatar-default {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background: #e0e0e0;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 40rpx;
		object-fit: cover;
	}

	.user-details {
		flex: 1;
	}

	.user-name {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 4rpx;
	}

	.user-phone {
		font-size: 24rpx;
		color: #999;
	}

	.btn-edit {
		background: #667eea;
		color: white;
		border: none;
		border-radius: 8rpx;
		padding: 10rpx 20rpx;
		font-size: 24rpx;
		white-space: nowrap;
	}

	.menu-section {
		background: white;
		margin: 0 20rpx;
		border-radius: 12rpx;
		overflow: hidden;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
	}

	.menu-item {
		display: flex;
		align-items: center;
		padding: 16rpx 20rpx;
		border-bottom: 1rpx solid #eee;
		gap: 16rpx;
	}

	.menu-item:last-child {
		border-bottom: none;
	}

	.menu-icon {
		font-size: 28rpx;
		min-width: 30rpx;
	}

	.menu-label {
		flex: 1;
		font-size: 28rpx;
		color: #333;
	}

	.menu-arrow {
		font-size: 28rpx;
		color: #ccc;
	}

	.logout-section {
		padding: 20rpx;
		margin-top: auto;
		margin-bottom: 20rpx;
	}

	.btn-logout {
		width: 100%;
		background: #e74c3c;
		color: white;
		border: none;
		border-radius: 8rpx;
		padding: 16rpx;
		font-size: 28rpx;
		font-weight: bold;
	}
</style>