package com.Rezistr;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;

import java.util.*;

public class MyUtils {
	public static Object translate(Term term) {
		if (term.isList()) {
			List<Object> list = new ArrayList<>();
			Iterator     i    = ((Struct) term).listIterator();
			while (i.hasNext()) {
				list.add(translate((Term) i.next()));
			}
			return list;
		} else {
			return term;
		}
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list =
				new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}

