### ViewStud是否加载过

1. ViewStub被inflate后就不会再布局中存在。所以每次在inflate的时候重新findViewById去页面中寻找一下ViewStub，如果返回值不为null则ViewStub没有被inflate过。

        // 每次在inflate之前都调用一遍findViewById
        ViewStub viewStub=  (ViewStub) findViewById(R.id.viewstub);
        if(viewStub!=null){
            viewStub.inflate();
        }

2. 利用ViewStub的parent来判断。当ViewStub被inflate后，getParent返回值是null

        复制代码
        //初始化，不必每次都调用
        ViewStub viewStub=  (ViewStub) findViewById(R.id.viewstub);
        ....


        if(viewStub.getParent() !=null){
            viewStub.inflate();
        }