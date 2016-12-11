package br.glaicon.agenda_aniversarios;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements android.support.v7.app.ActionBar.TabListener {

    ContatoDAO contatoDAO;
    ViewPager pager;
    android.support.v7.app.ActionBar actionBar;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    String mActivityTitle;
    PageAdapter fragmentPagerAdapter;
    MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_main);

        contatoDAO = ContatoDAO.getInstance(this);
        mActivityTitle = getTitle().toString();

        setSwipeView();
        setDrawer();

        if (savedInstanceState != null) {
            int tabPosition = savedInstanceState.getInt("tab");
            pager.setCurrentItem(tabPosition);
        }

        ImageView imageView = (ImageView) this.findViewById(R.id.imagemPessoa);
        RoundImage roundImage = new RoundImage(((BitmapDrawable) getResources().getDrawable(R.drawable.user)).getBitmap());

        imageView.setImageDrawable(roundImage);
    }

    private void setSwipeView() {
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                actionBar.setSelectedNavigationItem(position);

                if (item != null)
                    item.setVisible(position == 0);
            }
        });

        fragmentPagerAdapter = new PageAdapter(getSupportFragmentManager());
        pager.setAdapter(fragmentPagerAdapter);

        actionBar.setDisplayShowTitleEnabled(true);

        android.support.v7.app.ActionBar.Tab tab = actionBar.newTab().setText(getString(R.string.aniversariantes)).setTabListener(this);
        actionBar.addTab(tab);

        tab = actionBar.newTab().setText(getString(R.string.contatos)).setTabListener(this);
        actionBar.addTab(tab);
    }

    private void setDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{getString(R.string.usuario), getString(R.string.atualizar), getString(R.string.sobre)}));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getApplicationContext(), ContatoActivity.class);
                    startActivityForResult(intent, ContatoActivity.ADICIONAR);
                } else if (position == 2) {
                    Intent intent = new Intent(getApplicationContext(), SobreActivity.class);
                    startActivity(intent);
                }
            }
        });

        setDrawerToggle();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.menu);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            contatoDAO.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        contatoDAO.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        setCalendario(menu);
        setSearchView(menu);

        return true;
    }

    private void setCalendario(Menu menu) {
        boolean jaInstanciado = item != null;
        boolean valueVisibility = true;
        String tittle = "";
        if (jaInstanciado) {
            valueVisibility = item.isVisible();
            tittle = item.getTitle().toString();
        }

        item = menu.findItem(R.id.action_calendario);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Lista")) {
                    alterarMenuItem(item, "Calendário", true);
                } else {
                    alterarMenuItem(item, "Lista", false);
                }

                return false;
            }

            private void alterarMenuItem(MenuItem item, String titulo, boolean ativarVisibilidadeCalendario) {
                item.setTitle(titulo);

                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof PageFragment && ((PageFragment) fragment).getPosition() == 0 && ((PageFragment) fragment).listViewContatos != null) {
                        View calendarView = fragment.getView().findViewById(R.id.fragment_calendar);

                        if(ativarVisibilidadeCalendario){
                            calendarView.setVisibility(View.VISIBLE);
                            ((PageFragment) fragment).listViewContatos.setVisibility(View.INVISIBLE);
                        }
                        else{
                            calendarView.setVisibility(View.INVISIBLE);
                            ((PageFragment) fragment).listViewContatos.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        if (jaInstanciado) {
            item.setVisible(valueVisibility);
            item.setTitle(tittle);
        }
    }

    private void setSearchView(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_find);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(this.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    if (newText.isEmpty()) {
                        try {
                            for (Fragment pageFragment : getSupportFragmentManager().getFragments()) {
                                if (pageFragment instanceof PageFragment)
                                    ((PageFragment) pageFragment).refreshContatos();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ArrayList<Contato> contatosResult = new ArrayList<>();

                        try {
                            for (Contato contato : contatoDAO.buscarContatos(TipoOrdenacao.DATA)) {
                                if (contato.getNome().toUpperCase().contains(newText.toUpperCase()))
                                    contatosResult.add(contato);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        for (Fragment pageFragment : getSupportFragmentManager().getFragments()) {
                            if (pageFragment instanceof PageFragment)
                                ((PageFragment) pageFragment).addContato(contatosResult);
                        }
                    }

                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    return true;
                }
            };

            searchView.setOnQueryTextListener(queryTextListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ContatoActivity.ADICIONAR && resultCode == RESULT_OK) {
            Contato contato = (Contato) data.getSerializableExtra("contato");
            contatoDAO.addContato(contato);

            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof PageFragment) {
                    PageFragment pageFragment = (PageFragment) fragment;
                    pageFragment.addContato(contato);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("tab", pager.getCurrentItem());
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        pager.setCurrentItem(tab.getPosition());

        if (item != null)
            item.setVisible(tab.getPosition() == 0);
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}



