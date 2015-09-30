package windyoak.core;

import java.net.URL;

/**
 *
 * @author felix
 */
public interface PostsService
{

    /**
     * Gibt eine Liste mit allen kompletten Posts zurück.
     * 
     * @param url
     * @return
     */
    public RSSPosts getPosts(URL url) throws OakCoreException;
    
    /**
     * Gibt einen einzelnen kompletten Post mit bestimmter ID zurück.
     * 
     * Wenn die ID nicht gefunden wird, wird NULL zurückgegeben.
     * 
     * 
     * @param url
     * @param id
     * @return
     */
    public RSSPost getPostByID(URL url, String id) throws OakCoreException;
    
}
