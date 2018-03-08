### NIO与IO

- IO面向流，单向，读取时称为输入流，写入时称为输出流  

- NIO面向缓冲区，在通道传输，双向，缓冲区负责存储  

- IO在一个客户端read或write，无论数据是否有效，会一直阻塞

- NIO客户端的通道都会注册到选择器，只会将准备就绪的任务分配到服务端的线程执行，未准备就绪时是非阻塞的

### 缓冲区

- 存储数据，除boolean外，都提供了相应基础数据类型的缓冲区  
      ByteBuffer 最常用的字节缓冲区  
      CharBuffer  
      ShrotBuffer  
      IntBuffer  

- 缓冲区的获取方法：  
     allocate()  

- 存取缓冲区数据的方法：
      put()   
      get()  

- 缓冲区核心属性：  
      capacity 容量，缓冲区最大存储数据的容量，一旦声明不能改变  
      limit 界限，缓冲区中可操作数据的大小，limit后面的数据不能读写  
      position 位置，正在操作数据的位置  
      mark 标记，记录position的位置  

   > 0 <= mark <= position <= limit <= capacity

- 操作属性的方法：  
      flip() 切换到读模式，position = 0， limit = 已写入大小  
      rewind() 重复读数据，读完后可继续读  
      clear() 清空缓冲区  
      reset() position恢复到mark的位置  
      hasRemaining() 判断是否还有剩余数据  
      remaining() 可操作的数量 limit - position  

- 直接缓冲区与非直接缓冲区  
      非直接缓冲区：通过allocate()方法分配缓冲区，建立在JVM内存中  
      直接缓冲区：通过allocateDirect()方法分配直接缓冲区，将缓冲区建立在物理内存中，提高效率  

        ByteBuffer buf = ByteBuffer.allocate(1024);

        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        String str = "acde";
        buf.put(str.getBytes());

        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 读数据模式flip() position = 0 limit = length
        buf.flip();
        System.out.println("-----------flip()");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        byte[] dst = new byte[buf.limit()];
        buf.get(dst);
        System.out.println(new String(dst));
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //rewind() 重复读数据
        buf.rewind();
        System.out.println("-----------rewind()");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 清空缓冲区数据还在
        buf.clear();
        System.out.println("-----------clear()");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());            


		String str = "abcde";
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.put(str.getBytes());
		
		buf.flip();// 切换到读
		byte[] dst = new byte[buf.limit()];
		buf.get(dst, 0, 2);
		System.out.println(new String(dst, 0, 2));
		System.out.println(buf.position());
		
		// mark标记
		buf.mark();
		buf.get(dst, 2, 2);
		System.out.println(new String(dst, 2, 2));
		System.out.println(buf.position());
		
		// reset恢复到mark位置
		buf.reset();
		System.out.println(buf.position());
		
		// 判断缓冲区中是否还有剩余数据
		if(buf.hasRemaining()) {
			// 可以操作的数量
			System.out.println(buf.remaining());
		}

### 通道Channel 

内存与IO接口间通过DMA直接存储器，CPU申请权限后直接由DMA负责，频繁操作也是耗资源  
Channel是完全独立的处理器，附属于CPU  

- 通道主要实现类  
      java.nio.channels.Channel  
            FileChannel  文件  
            SocketChannel  网络  
            ServerSocketChannel  
            DatagramChannel  

- 获取通道  
     支持通道的类提供了getChannel()方法  
     本地IO
     FileInputStream/FileOutputStream  
     RandomAccessFile  
     网络IO
     Socket  
     ServerSocket  
     DatagramSocket  

     JDK1.7中的NIO2提供了静态方法open()  

     JDK1.7中的NIO2的Files工具类newByteChannel()  

- 通道配合缓冲区复制文件，使用非直接缓冲区    

        // 利用通道完成文件的复制
        private static void test1() throws Exception {
            FileInputStream fis = new FileInputStream("1.jpg");
            FileOutputStream fos = new FileOutputStream("2.jpg");
            
            // 获取通道
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();
            
            // 分配缓冲区大小
            ByteBuffer buf = ByteBuffer.allocate(1024);
            
            // 将通道数据存入缓冲区
            while(inChannel.read(buf) != -1) {
                // 将缓冲区数据写入通道
                buf.flip();// 读模式
                outChannel.write(buf);
                buf.clear();// 清空缓冲区
            }
            
            outChannel.close();
            inChannel.close();
            fos.close();
            fis.close();
        }

- 使用直接缓冲区复制文件，就是内存映射文件  
      内存映射文件(通道的map()方法)与allocateDirect方法相同  
      内存映射文件只有ByteBuffer支持  

        private static void test2() throws Exception {
            FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            // StandardOpenOption.CREATE 不管是否存在都创建   StandardOpenOption.CREATE_NEW 存在就报错
            FileChannel outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
            
            // 内存映射文件  与allocateDirect方法相同
            MappedByteBuffer inMappedBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
            MappedByteBuffer outMappedBuf = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
            
            // 直接堆缓冲区进行数据读写操作
            byte[] dst = new byte[inMappedBuf.limit()];
            inMappedBuf.get(dst);
            outMappedBuf.put(dst);
            
            outChannel.close();
            inChannel.close();
        }      

- 通道间数据传输  
      直接缓冲区方式        
      transferFrom()  
      transferTo()  

        //通道之间的数据传输(直接缓冲区)
        @Test
        public void test3() throws IOException{
            FileChannel inChannel = FileChannel.open(Paths.get("d:/1.mkv"), StandardOpenOption.READ);
            FileChannel outChannel = FileChannel.open(Paths.get("d:/2.mkv"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
            
            // 两者相同
            // inChannel.transferTo(0, inChannel.size(), outChannel);
            outChannel.transferFrom(inChannel, 0, inChannel.size());
            
            inChannel.close();
            outChannel.close();
        }

- 分散Scatter与聚集Gather  
      分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中  
      聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中  

        //分散和聚集
        @Test
        public void test4() throws IOException{
            RandomAccessFile raf1 = new RandomAccessFile("1.txt", "rw");
            
            //1. 获取通道
            FileChannel channel1 = raf1.getChannel();
            
            //2. 分配指定大小的缓冲区
            ByteBuffer buf1 = ByteBuffer.allocate(100);
            ByteBuffer buf2 = ByteBuffer.allocate(1024);
            
            //3. 分散读取
            ByteBuffer[] bufs = {buf1, buf2};
            channel1.read(bufs);
            
            for (ByteBuffer byteBuffer : bufs) {
                byteBuffer.flip();
            }
            
            System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
            System.out.println("-----------------");
            System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));
            
            //4. 聚集写入
            RandomAccessFile raf2 = new RandomAccessFile("2.txt", "rw");
            FileChannel channel2 = raf2.getChannel();
            
            channel2.write(bufs);
        }      

### 字符集

- Charset 编码：字符串-->字节数组  解码：字节数组-->字符串  

        public static void test() throws Exception{
            Map<String, Charset> map = Charset.availableCharsets();
            Set<Entry<String, Charset>> set = map.entrySet();
            for(Entry<String, Charset> entry : set) {
                System.out.println(entry.getKey() + " -- " + entry.getValue());
            }
        }

        public static void main(String[] args) throws Exception {
            // 指定编码
            Charset cs1 = Charset.forName("GBK");
            // 获取编码器与解码器
            CharsetEncoder ce = cs1.newEncoder();
            CharsetDecoder cd = cs1.newDecoder();
            CharBuffer cBuf = CharBuffer.allocate(1024);
            cBuf.put("你好");
            cBuf.flip();
            
            // 编码
            ByteBuffer bBuf = ce.encode(cBuf);
            for(int i = 0;i<4;i++) {
                System.out.println(bBuf.get());
            }
            bBuf.flip();
            // 解码
            cBuf = cd.decode(bBuf);
            System.out.println(cBuf.toString());
        }        

### NIO阻塞Socket

        @Test
        public void client() throws Exception {
            // 1、获取通道
            SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

            // 2、获取缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            
            // 3、本地读取图片，发送给服务端
            FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
            while(inChannel.read(buf) != -1) {
                buf.flip();
                sChannel.write(buf);
                buf.clear();
            }
            
            // 4、关闭通道
            inChannel.close();
            sChannel.close();
        }

        @Test
        public void server() throws Exception {
            // 1、获取通道
            ServerSocketChannel ssChannel = ServerSocketChannel.open();
            
            // 2、绑定连接
            ssChannel.bind(new InetSocketAddress(9898));
            
            // 3、获取客户端连接
            SocketChannel sChannel = ssChannel.accept();
            
            // 4、分配缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            
            // 5、接收客户端数据，保存本地
            FileChannel outChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            while(sChannel.read(buf)!= -1) {
                buf.flip();
                outChannel.write(buf);
                buf.clear();
            }
            
            // 6、关闭通道
            sChannel.close();
            outChannel.close();
            ssChannel.close();
        }        

### NIO非阻塞Socket

        @Test
        public void client() throws IOException {
            // 1、获取通道
            SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8800));
            
            // 2、切换非阻塞模式
            sChannel.configureBlocking(false);
            
            // 3、分配指定大小的缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            
            // 4、发送数据
            buf.put(LocalDateTime.now().toString().getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
            
            // 5、关闭通道
            sChannel.close();
        }

        @Test
        public void server() throws IOException {
            // 获取通道
            ServerSocketChannel ssChannel = ServerSocketChannel.open();
            
            // 设置非阻塞模式
            ssChannel.configureBlocking(false);
            
            // 绑定连接
            ssChannel.bind(new InetSocketAddress(8800));
            
            // 获取选择器
            Selector selector = Selector.open();

            // 通道注册到选择器
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            // 获取选择器上准备就绪的事件
            while(selector.select() > 0) {
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while(it.hasNext()) {
                    SelectionKey sk = it.next();
                    
                    // 接收就绪
                    if(sk.isAcceptable()) {
                        SocketChannel sChannel = ssChannel.accept();
                        
                        // 客户端切换非阻塞
                        sChannel.configureBlocking(false);
                        
                        // 将该通道注册到选择器
                        sChannel.register(selector, SelectionKey.OP_READ);
                    }else 
                    // 读就绪	
                    if(sk.isReadable()) {
                        SocketChannel sChannel = (SocketChannel) sk.channel();
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        int len = 0;
                        while((len = sChannel.read(buf))>0) {
                            buf.flip();
                            System.out.println(new String(buf.array(), 0, len));
                            buf.clear();
                        }
                    }
                    
                    // 使用完取消选择键
                    it.remove();
                }
            }
        }        