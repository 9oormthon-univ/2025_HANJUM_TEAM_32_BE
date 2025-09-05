package com.hanjum.newshanjumapi.domain.article.util;

import com.hanjum.newshanjumapi.domain.article.entity.Article;

public class DefaultImageUtil {
    public static String getDefaultImage(Article.Category category){
        return switch(category){
            case POLITICS -> "https://plus.unsplash.com/premium_photo-1683141498413-cdfc0feccdb3?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8JUVDJUEwJTk1JUVDJUI5JTk4fGVufDB8fDB8fHww";
            case ECONOMY -> "https://plus.unsplash.com/premium_photo-1681487769650-a0c3fbaed85a?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8JUVBJUIyJUJEJUVDJUEwJTlDfGVufDB8fDB8fHww";
            case SOCIETY-> "https://plus.unsplash.com/premium_photo-1681910115431-29a1c8876e5a?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8JUVDJTgyJUFDJUVEJTlBJThDfGVufDB8fDB8fHww";
            case CULTURE-> "https://plus.unsplash.com/premium_photo-1670745084868-7b4f727cc934?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8JUVCJUFDJUI4JUVEJTk5JTk0fGVufDB8fDB8fHww";
            case IT-> "https://images.unsplash.com/photo-1573495628363-04667cedc587?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8SVR8ZW58MHx8MHx8fDA%3D";
            case WORLD-> "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8JUVDJTg0JUI4JUVBJUIzJTg0fGVufDB8fDR8fHww";
            default -> "https://images.unsplash.com/photo-1751778657219-29b89e5859bc?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8ZGVmYXVsdHxlbnwwfHwwfHx8MA%3D%3D";
        };
    }
}
