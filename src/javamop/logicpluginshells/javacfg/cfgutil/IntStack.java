package javamop.logicpluginshells.javacfg.cfgutil;

public class IntStack implements java.io.Serializable {
   int[] data;
   int curr_index = 0;

   public IntStack(){
      data = new int[32];
   }

   public String toString(){
      String ret = "[";
      for (int i = curr_index; i>=0; i--)
         ret += Integer.toString(data[i])+",";
      return ret+"]";
   }

   public int hashCode() {
      return curr_index^peek();
   }

   public boolean equals(Object o) {
      if (o == null) return false;
      if (!(o instanceof IntStack)) return false;
      IntStack s = (IntStack)o;
      if(curr_index != s.curr_index) return false;
      for(int i = 0; i < curr_index; i++)
         if(data[i] != s.data[i]) return false;
      return true;
   }

   public IntStack(int size){
      data = new int[size];
   }

   public int peek(){
      return data[curr_index - 1];
   }

   public int pop(){
      return data[--curr_index];
   }

   public void pop(int num){
      curr_index -= num;
   }

   public void push(int datum){
      if(curr_index < data.length)
         data[curr_index++] = datum;
      else{
         int len = data.length;
         int[] old = data;
         data = new int[len * 2];
         for(int i = 0; i < len; ++i){
            data[i] = old[i];
         }
         data[curr_index++] = datum; 
      }
   }

   public IntStack fclone(){
      IntStack ret = new IntStack(data.length);
      ret.curr_index = curr_index;
      for(int i = 0; i < curr_index; ++i){
         ret.data[i] = data[i];
      }
      return ret;
   }

   public void clear(){
      curr_index = 0;
   }
}
