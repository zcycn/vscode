### 主题样式
- android:windowBackgroud  全局背景  
- android:navigationBarColor  back/home/menu导航栏背景，5.0以上
- values-v14、values-v22，那么在21版本的系统会调用v14  

### AlertDialog

    AlertDialog.Builder builder = new Builder(this);
    builder.setTitle("title");
    builder.setMessage("message");
    builder.setPositiveButton("确定", new OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which){

        }
    });
    builder.setNegativeButton("取消", new OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which){

        }
    });
    builder.show();

> android.app.AlertDialog显示的对话框与系统版本相关  
> android.support.v7.app.AlertDialog可以兼容5.0以上样式  

### AppCompat兼容控件
可以使AppCompat主题兼容低版本  
- android.support.v7.widget.AppCompatButton  
- android.support.v7.widget.AppCompatEditText  

### SwipeRefreshLayout

    <android.support.v4.widget.SwipeRefreshLayout
        ...
        >

    </android.support.v4.widget.SwipeRefreshLayout>

    srl.setSize(SwipeRefreshLayout.LARGE);
    srl.setOnRefreshListener(new OnRefreshListener(){
        @Override
        public void onRefresh(){
            srl.setRefreshing(false);
        }
    });
    // 进度条颜色
    srl.setColorSchemeColor(Color.RED, Color.BLUE);
    // 进度条背景颜色
    srl.setProgressBackgroudColorSchemeColor(Color.YELLOW);
    // 设置下拉距离
    srl.setDistanceToTriggerSync(70);

### PopupWindow

    String[] items = new String(){"", "", ""};
    ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
    final ListPopupWindow pop = new ListPopupWindow(this);
    pop.setAdapter(adapter);
    pop.setAnchorView(view);// 相对View的位置显示
    pop.setWidth(200);
    pop.setHeight(500);
    pop.show();
    pop.setOnItemClickListener(new OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            pop.dismiss();
        }
    })

> 效果类似Toolbar更多菜单效果    

    PopupMenu pop = new PopupMenu(this, view);// 瞄点
    pop.getMenuinflater().inflate(R.menu.menu, pop.getMenu());// 往menu里渲染
    pop.show();

> 效果同上  
    