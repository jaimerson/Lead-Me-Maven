/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_dados;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author rafao
 */
public abstract class AbstractDAO<T, I extends Serializable> {

    public static final String DIRETORIO_RECURSOS = "";

    protected EntityManager em;
    protected Class<T> classe;

    public AbstractDAO(Class<T> classe) {
        EntityManagerFactory factory = FabricaEM.getEntityManagerFactory();
        em = factory.createEntityManager();
        this.classe = classe;
    }

    public synchronized T salvar(T entity) throws PersistenceException{
        EntityTransaction t = em.getTransaction();
        t.begin();
        try{
            em.persist(entity);
            em.flush();
            t.commit();
        }catch(PersistenceException e){
            t.rollback();
            throw new PersistenceException(e.getLocalizedMessage());
        }
        return entity;
    }

    public synchronized T atualizar(T entity) {
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.merge(entity);
        em.flush();
        t.commit();
        return entity;
    }

    public synchronized void remover(I id) {
        T entity = encontrar(id);
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        T mergedEntity = em.merge(entity);
        em.remove(mergedEntity);
        em.flush();
        tx.commit();
    }

    public List<T> listar() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(classe);
        query.from(classe);
        return em.createQuery(query).getResultList();
    }

    public T encontrar(I id) {
        return em.find(classe, id);
    }
    
    public synchronized boolean conexaoAberta(){
        return em.isOpen();
    }
    
    public synchronized void fecharConexao(){
        if(conexaoAberta()){
            em.close();
            System.out.println("Fechando conexao");
        }
    }
}