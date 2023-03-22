package telran.net.test;

import java.util.Arrays;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class ProtocolCalculator implements Protocol {

	@Override
	public Response getResponse(Request request) {
		Response res;
		int defaultResData = -1;
		if (request.data instanceof Operands) {
			if (Arrays.stream(Calculator.values()).anyMatch(c -> c.name().equals(request.type))) {
				res = new Response(ResponseCode.WRONG_REQUEST, defaultResData);
				int a = ((Operands) request.data).getA();
				int b = ((Operands) request.data).getB();
				int resCalc = switch (Calculator.valueOf(request.type)) {
				case PLUS -> a + b;
				case MINUS -> a - b;
				case MULTIPLY -> a * b;
				case DIVIDE -> a / b;
				};
				
				res = new Response(ResponseCode.OK, resCalc);				
			} else {
				res = new Response(ResponseCode.WRONG_REQUEST, defaultResData);
			}
		} else {
			res = new Response(ResponseCode.WRONG_DATA, defaultResData);
		}
		return res;
	}

}