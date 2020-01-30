package com.aefyr.flexfilter.applier;

import com.aefyr.flexfilter.config.core.ComplexFilterConfig;
import com.aefyr.flexfilter.config.core.FilterConfig;

import java.util.ArrayList;
import java.util.List;

public class FilterApplier<T> {

    private ComplexFilterConfig mFilterConfig;
    private CustomFilterFactory<T> mFactory;

    public FilterApplier(ComplexFilterConfig config, CustomFilterFactory<T> factory) {
        mFilterConfig = config;
        mFactory = factory;
    }

    public List<T> filter(List<T> list) {
        List<T> filteredList = new ArrayList<>();

        ArrayList<CustomFilter<T>> customFilters = new ArrayList<>();

        for (FilterConfig filterConfig : mFilterConfig.filters()) {
            customFilters.add(mFactory.createCustomFilter(filterConfig));
        }

        for (T t : list) {
            boolean filteredOut = false;

            for (CustomFilter<T> customFilter : customFilters) {
                if (customFilter.filterSimple(t)) {
                    filteredOut = true;
                    break;
                }
            }

            if (!filteredOut) {
                filteredList.add(t);
            }
        }

        for (CustomFilter<T> customFilter : customFilters) {
            filteredList = customFilter.filterComplex(filteredList);
        }

        return filteredList;
    }


}
