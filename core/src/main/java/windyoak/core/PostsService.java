package windyoak.core;

import java.net.URL;

/**
 *
 * @author felix
 */
public interface PostsService
{
    
    /**
     * Liefert alle Posts zu einem bestimmten Projekt, sofern eine RSS- oder ATOM-URL eingetragen ist.
     * 
     * 
     * @param url
     * @return
     * @throws OakCoreException
     */
    public RSSPosts getAllPosts(URL url) throws OakCoreException;
    
    /**
     * Wie getAllPosts(), liefert aber nur die letzten n Posts zurück.
     * 
     * Kann für den /recent/posts Pfad verwendet werden, welcher aber nicht Teil des Belegs ist.
     * 
     * 
     * @param url
     * @param n
     * @return
     * @throws OakCoreException
     */
    public RSSPosts getRecentPosts(URL url, int n) throws OakCoreException;
    
    /**
     * Gibt eine Liste mit allen kompletten Posts zurück.
     * 
     * @param url
     * @return
     */
    public RSSPosts getPosts(URL url, boolean recent, int count) throws OakCoreException;
    
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
