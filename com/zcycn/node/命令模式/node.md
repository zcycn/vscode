### 命令模式

1. 将请求封装成对象，用户使用不同的请求把客户端参数化

2. 把发出命令的责任和执行命令的责任分开  

　　命令模式涉及到五个角色，它们分别是：

　　●　　客户端(Client)角色：创建一个具体命令(ConcreteCommand)对象并确定其接收者。

　　●　　命令(Command)角色：声明了一个给所有具体命令类的抽象接口。

　　●　　具体命令(ConcreteCommand)角色：定义一个接收者和行为之间的弱耦合；实现execute()方法，负责调用接收者的相应操作。execute()方法通常叫做执行方法。

　　●　　请求者(Invoker)角色：负责调用命令对象执行请求，相关的方法叫做行动方法。

　　●　　接收者(Receiver)角色：负责具体实施和执行一个请求。任何一个类都可以成为接收者，实施和执行请求的方法叫做行动方法。

### 类图

![command](resource/command.png)    

1. 接收者 

        public class Receiver {
            /**
            * 真正执行命令相应的操作
            */
            public void action(){
                System.out.println("执行操作");
            }
        }

2. 抽象命令

        public interface Command {
            /**
            * 执行方法
            */
            void execute();
        }        

3. 具体命令

        public class ConcreteCommand implements Command {
            //持有相应的接收者对象
            private Receiver receiver = null;
            /**
            * 构造方法
            */
            public ConcreteCommand(Receiver receiver){
                this.receiver = receiver;
            }
            @Override
            public void execute() {
                //通常会转调接收者对象的相应方法，让接收者来真正执行功能
                receiver.action();
            }

        }        

4. 请求者

        public class Invoker {
            /**
            * 持有命令对象
            */
            private Command command = null;
            /**
            * 构造方法
            */
            public Invoker(Command command){
                this.command = command;
            }
            /**
            * 行动方法
            */
            public void action(){
                
                command.execute();
            }
        }        

5. 客户端        

        public class Client {

            public static void main(String[] args) {
                //创建接收者
                Receiver receiver = new Receiver();
                //创建命令对象，设定它的接收者
                Command command = new ConcreteCommand(receiver);
                //创建请求者，把命令对象设置进去
                Invoker invoker = new Invoker(command);
                //执行方法
                invoker.action();
            }

        }