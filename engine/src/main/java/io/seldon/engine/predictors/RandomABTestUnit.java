package io.seldon.engine.predictors;

import java.util.Random;

import org.springframework.stereotype.Component;

import io.seldon.engine.exception.APIException;
import io.seldon.protos.PredictionProtos.SeldonMessage;


@Component
public class RandomABTestUnit extends PredictiveUnitBean {
	
	Random rand = new Random(1337);

	@Override
	public int route(SeldonMessage input, PredictiveUnitState state){
		@SuppressWarnings("unchecked")
		PredictiveUnitParameter<Float> parameter = (PredictiveUnitParameter<Float>) state.parameters.get("ratioA");
		
		if (parameter == null){
			throw new APIException(APIException.ApiExceptionType.ENGINE_INVALID_ABTEST,"Parameter 'ratioA' is missing.");
		}
		
		Float ratioA = parameter.value;
		Float comparator = rand.nextFloat();
		
		if (state.children.size() != 2){
			throw new APIException(APIException.ApiExceptionType.ENGINE_INVALID_ABTEST,String.format("AB test has %d children ",state.children.size()));
		}
		
		//FIXME Possible bug : keySet is not ordered as per the definition of the AB test
		if (comparator<=ratioA){
			// We select model A
			return 0;
		}
		else{
			return 1;
		}
	}
}
