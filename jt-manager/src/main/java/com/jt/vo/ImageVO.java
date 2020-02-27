package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain =true)
public class ImageVO {
    private Integer error;//0正常 1失败
    private String url;//图片虚拟路径
    private Integer width;//宽度
    private Integer height;//高度
   public static ImageVO fail(){
       return new ImageVO(1,null,null,null);
   }
    public static ImageVO success(String url,Integer width,Integer height){
        return new ImageVO(0,url,width,height);
    }
}
