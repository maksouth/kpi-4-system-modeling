package lab3;

import lab3.car_bank.CarBankModel;

public class Simulation {
    public static void main(String[] args) {

//        Process first = new Process(4, 2, () -> FunRand.Exp(0.3));
//        Process second = new Process(4, 2, () -> FunRand.Exp(0.3));
//        Create generator = new Create(0.1, () -> FunRand.Exp(0.5));

        CarBankModel model = new CarBankModel();

        model.simulate(100);
    }
}
