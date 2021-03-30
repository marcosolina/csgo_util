package com.marco.csgoutil.roundparser.services.implementations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.marco.csgoutil.roundparser.services.interfaces.CsgoRoundFileParser;
import com.marco.csgoutil.roundparser.services.interfaces.RoundFileService;
import com.marco.csgoutil.roundparser.services.interfaces.RoundsService;
import com.marco.utils.MarcoException;

public class RoundsServiceMarco implements RoundsService {
	private static final Logger _LOGGER = LoggerFactory.getLogger(RoundsServiceMarco.class);

	@Autowired
	private RoundFileService roundFildeService;
	@Autowired
	private CsgoRoundFileParser roundParserService;

	@Override
	public Map<String, Double> processAllDemFiles() throws MarcoException {
		List<File> fileList = roundFildeService.retrieveAllDemFiles();

		Map<String, Double> avarageMap = new HashMap<>();

		List<Map<String, Integer>> listMap = new ArrayList<>();
		
		Map<String, Integer> userGamesPlayer = new HashMap<>();
		
		fileList.parallelStream().forEach(f -> {
			try {
				listMap.add(roundParserService.extractPlayersScore(f));
			} catch (MarcoException e) {
				_LOGGER.error(String.format("Could not process DEM file: %s", f.getAbsoluteFile()));
			}
		});
		
		int size = listMap.size();
		if(size > 0) {
			// @formatter:off
			listMap.stream().forEach(m -> 
				m.keySet().stream().forEach(k -> {
					userGamesPlayer.compute(k , (key, v) -> v == null ? 1 : v + 1);
					avarageMap.compute(k, (key, v) -> {
						if(v == null) {
							v = Double.valueOf(0);
						}
						v = v + Double.valueOf(m.get(key));
						return v;
					});
				})
			);
			// @formatter:on
			avarageMap.keySet().stream().forEach(k -> avarageMap.compute(k, (key, v) -> v / userGamesPlayer.get(key)));
		}

		return avarageMap;
	}

}
