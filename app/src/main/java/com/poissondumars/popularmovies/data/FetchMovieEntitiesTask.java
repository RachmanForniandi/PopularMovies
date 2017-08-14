package com.poissondumars.popularmovies.data;

import android.os.AsyncTask;

import com.poissondumars.popularmovies.api.TheMoviesDbApiClient;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

interface FetchMovieEntitiesTaskListener<T> {

    void entitiesDidLoad(T[] entities);

}

public class FetchMovieEntitiesTask<T> extends AsyncTask<Integer, Void, T[]> {

    private FetchMovieEntitiesTaskListener<T> mListener;

    public FetchMovieEntitiesTask(FetchMovieEntitiesTaskListener<T> listener) {
        mListener = listener;
    }

    @Override
    protected T[] doInBackground(Integer... params) {
        Integer movieId = params[0];

        try {
            Type superclass = getClass().getGenericSuperclass();
            Type tType = ((ParameterizedType) superclass).getActualTypeArguments()[0];
            String className = tType.toString().split(" ")[1];
            Class entityClass = Class.forName(className);

            String jsonResponse = TheMoviesDbApiClient.getMovieEntities(entityClass, movieId);

            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(T[] entities) {
        super.onPostExecute(entities);


    }
}
