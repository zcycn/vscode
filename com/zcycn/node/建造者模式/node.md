### 建造者模式

- 目的是构建与表示进行分离  
- 完整的建造者模式有四个角色 

![Build](resource/build.png)     

- 精简建造模式

        /**
        * 建造者和产品
        * @author zhuch
        *
        */
        public class Deformation {

            private String window;
            private String floor;
            
            public void print(){
                System.out.println(window + floor);
            }
            
            static class Build{
                private String window;
                private String floor;
                
                public Build setWindow(String window){
                    this.window = window;
                    return this;
                }
                
                public Build setFloor(String floor){
                    this.floor = floor;
                    return this;
                }
                
                public Deformation build(){
                    Deformation d = new Deformation();
                    d.window = window;
                    d.floor = floor;
                    return d;
                }
            }
        }

- Android的Dialog就采用这种设计模式