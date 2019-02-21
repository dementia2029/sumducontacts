package com.dementia.sumdu.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dementia.sumdu.R;
import com.dementia.sumdu.data.Contact;
import com.dementia.sumdu.data.ContactData;
import com.dementia.sumdu.data.Pidrozdil;
import com.dementia.sumdu.library.PinnedHeaderListView;
import com.dementia.sumdu.utils.DBManager;
import com.dementia.sumdu.utils.DataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class PlaceholderFragment extends Fragment {

    //public static ArrayList<BookData> groupAdvices;
    //public static ArrayList<ChapterData> popular;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    private LayoutInflater mInflater;
    private PinnedHeaderListView mListView;
    private ContactsAdapter mAdapter;
    private  AllContactsAdapter allContactsAdapter;
    private FavAdapter favAdapter;
    DBManager dbManager = DBManager.getInstance(getActivity());

    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private int currentFragmentId;


    public void update() {
        if (currentFragmentId == 2 && mAdapter != null) {
            mAdapter.updateData(getContacts());
        } else if (currentFragmentId == 1 && favAdapter != null) {
            try {
                dbManager.open();
                favAdapter.setNewReciepts(dbManager.getAllFavs());
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                dbManager.close();
            }

        }
        else if (currentFragmentId == 3 && allContactsAdapter != null) {
            try {
                dbManager.open();
                favAdapter.setNewReciepts(dbManager.getAnother());
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                dbManager.close();
            }

        }/* else if (currentFragmentId == 3 && favoritesAdapter != null) {
            favoritesAdapter.update(mainData.getFavorites());
        }
        */
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = new View(this.getActivity());
        int argNumber = getArguments().getInt(ARG_SECTION_NUMBER, 0);
        //MainData mainData = DataManager.getInstance().getMainData();

        switch (argNumber) {
            case 1:
                currentFragmentId = 1;
                //groupAdvices = mainData.getGroupCouncilByThemes();

                rootView = inflater.inflate(R.layout.tab2, container, false);
                RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerViewListFav);
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                try {
                    dbManager.open();
                    favAdapter = new FavAdapter(dbManager.getAllFavs(),getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    dbManager.close();
                }
                recyclerView.setAdapter(favAdapter);


                /*ExpandableListView listExpand = (ExpandableListView) rootView.findViewById(R.id.listExpand);
                listExpand.setDividerHeight(0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    listExpand.setNestedScrollingEnabled(true);
                }

                listExpand.setGroupIndicator(null);
                listExpand.setChildIndicator(null);

                groupAdapterNew = new Column2FirstLevelAdapter(groupAdvices, getActivity());
                listExpand.setAdapter(groupAdapterNew);
                */

                break;
            case 2:
                currentFragmentId = 2;
                //popular = mainData.getPopularCouncils();

                rootView = inflater.inflate(R.layout.tab1, container, false);
                final ArrayList<Contact> contacts = getContacts();
                Collections.sort(contacts, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact lhs, Contact rhs) {
                        char lhsFirstLetter = TextUtils.isEmpty(lhs.displayName) ? ' ' : lhs.displayName.charAt(0);
                        char rhsFirstLetter = TextUtils.isEmpty(rhs.displayName) ? ' ' : rhs.displayName.charAt(0);
                        int firstLetterComparison = Character.toUpperCase(lhsFirstLetter) - Character.toUpperCase(rhsFirstLetter);
                        if (firstLetterComparison == 0)
                            return lhs.displayName.compareTo(rhs.displayName);
                        return firstLetterComparison;
                    }
                });
                mListView = (PinnedHeaderListView) rootView.findViewById(R.id.list6);
                mAdapter = new ContactsAdapter(contacts,getActivity());

                int pinnedHeaderBackgroundColor = getResources().getColor(getResIdFromAttribute(getActivity(), android.R.attr.colorBackground));
                mAdapter.setPinnedHeaderBackgroundColor(pinnedHeaderBackgroundColor);
                mAdapter.setPinnedHeaderTextColor(getResources().getColor(android.R.color.white));
                mListView.setPinnedHeaderView(inflater.inflate(R.layout.pinned_header_listview_side_header, mListView, false));
                mListView.setAdapter(mAdapter);
                mListView.setOnScrollListener(mAdapter);
                mListView.setEnableHeaderTransparencyChanges(false);




                /*ExpandableListView listExpand2 = (ExpandableListView) rootView.findViewById(R.id.listExpand);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    listExpand2.setNestedScrollingEnabled(true);
                }

                listExpand2.setGroupIndicator(null);
                listExpand2.setChildIndicator(null);
                listExpand2.setChildDivider(getResources().getDrawable(android.R.color.transparent));
                listExpand2.setDivider(getResources().getDrawable(R.color.separator));
                listExpand2.setDividerHeight(0);

                groupAdapter = new Column1FirstLevelAdapter(popular, getActivity());
                listExpand2.setAdapter(groupAdapter);

                listExpand2.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                                int childPosition, long id) {
                        getActivity().startActivity(ActivityAdvice.getIntent(getActivity(), popular.get(groupPosition), childPosition));
                        return true;
                    }
                });
                */
                break;
            case 3:
                currentFragmentId = 2;
                //popular = mainData.getPopularCouncils();

                rootView = inflater.inflate(R.layout.tab3, container, false);

                PinnedHeaderListView pinnedHeaderListView = (PinnedHeaderListView)rootView.findViewById(R.id.list10);
                ArrayList<ContactData> arrayList=new ArrayList<>();
                try {
                    dbManager.open();
                    arrayList = dbManager.getAnother();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    dbManager.close();
                }
                ArrayList<Contact> array2 = new ArrayList();
                for (int i = 0; i < arrayList.size(); ++i) {
                    Contact contact = new Contact();
                    contact.contactId = arrayList.get(i).getId();
                    contact.displayName = arrayList.get(i).getName();
                    contact.underText = "";
                    array2.add(contact);
                }
                allContactsAdapter = new AllContactsAdapter(array2,getActivity(),-1,-1);
                pinnedHeaderListView.setAdapter(allContactsAdapter);


                /*ExpandableListView listExpand2 = (ExpandableListView) rootView.findViewById(R.id.listExpand);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    listExpand2.setNestedScrollingEnabled(true);
                }

                listExpand2.setGroupIndicator(null);
                listExpand2.setChildIndicator(null);
                listExpand2.setChildDivider(getResources().getDrawable(android.R.color.transparent));
                listExpand2.setDivider(getResources().getDrawable(R.color.separator));
                listExpand2.setDividerHeight(0);

                groupAdapter = new Column1FirstLevelAdapter(popular, getActivity());
                listExpand2.setAdapter(groupAdapter);

                listExpand2.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                                int childPosition, long id) {
                        getActivity().startActivity(ActivityAdvice.getIntent(getActivity(), popular.get(groupPosition), childPosition));
                        return true;
                    }
                });
                */
                break;
        }

        return rootView;
    }

    public ArrayList<Contact> getContacts(){
        DataManager dataManager = DataManager.getInstance();
        LinkedHashMap<Integer, Pidrozdil> alldata = dataManager.getlldata();
        ArrayList<Pidrozdil> array = new ArrayList(alldata.values());
        ArrayList<Contact> array2 = new ArrayList();
        for (int i = 0; i < array.size(); ++i) {
            Contact contact = new Contact();
            contact.contactId = array.get(i).getId();
            contact.displayName = array.get(i).getPartition();
            contact.underText = array.get(i).getCategory();
            array2.add(contact);
        }
        return array2;
    }

    public static int getResIdFromAttribute(final Activity activity, final int attr) {
        if (attr == 0)
            return 0;
        final TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId;
    }
}
