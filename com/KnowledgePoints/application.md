### Application前后台切换

有关Home键
每个Android开发者都曾经问个这问题，“我能不能覆盖Home键？”。答曰：

当然不行！处理返回按钮已经足够艰难了，如果Home键能覆盖那就更是一团糟。
真相如下: 
- 如果Home键没有返回桌面，用户会很失望 
- 用户会困在app中 
- 覆盖不了所有情况 
1.由于来电而退出app的情况； 
2.挂电话的情况； 
3.点击切换app按钮的情况。

所以，你很可能是想知道用户什么时候离开你的应用的，而不是把用户困在应用中。如果Application中有onStop()方法就相当简单啦，对不对？

Application中遗失的onStart()和onStop()
想一想，为什么在onStop()方法中停止？让我们深入了解前台/后台(forground/background)的生命周期，当状态发生改变时，能了然于胸。

为什么需要知道当前的状态呢？假设我们的应用会收到一条通知。如果当时应用处于前台运行，肯定会展示出应用内相应的通知界面。相反，如果应用处于后台运行，那在通知栏进行展示就比较合适。

例如其他情况，你想知道应用从前台运行到切换至后台的会话长短，又或者当用户需要处理其他事务离开时，你需要把你应用的缓存清空。

谢天谢地，你可以使用可靠的方式获取这些信息，而不是使用令人抓狂的ActivityManager.getRunningTask,也不是令人蛋疼的Activity的生命周期（onStop()总是不紧跟onStart()调用）。

应用切换至后台
从API 14(Android 4.0 ICS)，我们可以调用Application.onTrimMemory(int level)，这个方法包含了一个等级叫TRIM_MEMORY_UI_HIDDEN，用于记录应用即将进入后台运行。

下面是一个自定义Applicaiton的使用

public class MyApplication extends Application 
{ 
    @Override 
      public void onTrimMemory(int level) { 
          super.onTrimMemory(level); 
          if (level == TRIM_MEMORY_UI_HIDDEN) { 
              isBackground = true;
              notifyBackground(); 
        } 
     } 
}
啊哈！这样就能知道应用切换至后台运行啦。

手机熄屏
Application.onTrimMemory(int level)在手机熄屏时不回调怎么办？用Intent.ACTION_SCREEN_OFF 
注册BroadcastReceiver

    public class MyApplication extends Application {
      // ...
      @Override
        public void onCreate() {
            super.onCreate();
            // ...
            IntentFilter screenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                  if (isBackground) {
                      isBackground = false;
                      notifyForeground();
                  }
                }
            }, screenOffFilter);
          }
    }
注意：无需监听屏幕点亮的动作，下面会全部搞掂。

应用切换至前台
没有任何flag或者trim level来判断应用切换至前台，覆写Activity.onResume()是最好的方法。在基类Activity中复写它是一个选择，但无须如此。

一个更简洁的做法是，利用Application.registerActivityLifeStyleCallbacks()，如名字描述一样，可以覆写每一个生命周期函数。在这个例子中，在不侵入式改动每个Activity的代码的前提下，在Activity.onResume()中执行了代码。

下面是一个自定义Application：

    public class MyApplication extends Application {
        // ...
        @Override
           public void onCreate() {
               super.onCreate();

               registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                   // ...
                   @Override
                   public void onActivityResumed(Activity activity) {
                     if (isBackground) {
                         isBackground = false;
                         notifyForeground();
                     }
                   }
                   // ...
               });
           }
           // ...
    }
组织起来
下面是一个应用前后台切换的完整例子。

    public class MyApplication extends Application {

        // Starts as true in order to be notified on first launch
        private boolean isBackground = true;

        @Override
        public void onCreate() {
            super.onCreate();

            listenForForeground();
            listenForScreenTurningOff();
        }

        private void listenForForeground() {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                //...
                @Override
                public void onActivityResumed(Activity activity) {
                    if (isBackground) {
                        isBackground = false;
                        notifyForeground();
                    }
                }
                //...
            });
        }

        private void listenForScreenTurningOff() {
            IntentFilter screenStateFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    isBackground = true;
                    notifyBackground();
                }
            }, screenStateFilter);
        }

        @Override
        public void onTrimMemory(int level) {
            super.onTrimMemory(level);
            if (level == TRIM_MEMORY_UI_HIDDEN) {
                isBackground = true;
                notifyBackground();
            }

        }

        private void notifyForeground() {
            // This is where you can notify listeners, handle session tracking, etc
        }

        private void notifyBackground() {
            // This is where you can notify listeners, handle session tracking, etc
        }

        public boolean isBackground() {
          return isBackground;
        }
    }
总结
API 14及以上
用Application.onTrimLevel(int level)和TRIM_MEMORY_UI_HIDDEN判断应用是否切换至后台运行。
通过INTENT.ACTION_SCREEN_OFF注册广播接受器监听屏幕熄灭
注册Activity.registerLifeStyleCallback监听应用切换至前台运行