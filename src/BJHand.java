

public class BJHand extends Hand{

        private boolean isBJ;
        private boolean isFinished;
        private boolean isBusted;
        private int finalBet;
        private int timesSplit;
        private BJHand splitFrom;
        private boolean choiceLocked;

        public BJHand(int num, Deck deck, int unit) {
            super(num, deck);
            this.finalBet = unit;
            timesSplit = 0;
            splitFrom = null;
            choiceLocked = false;
        }

        public boolean isBJ() {
            return isBJ;
        }

        public void setBJ() {
            this.isBJ = true;
            this.isFinished = true;
        }

        public void setSplitFrom(BJHand splitFrom) {this.splitFrom = splitFrom;}

        public BJHand getSplitFrom() {return this.splitFrom;}

        public void addTimesSplit() {this.timesSplit++;}

        public int getTimesSplit() {return timesSplit;}

        public boolean isFinished() {
            return isFinished;
        }

        public void setFinished() {
            this.isFinished = true;
        }

        public boolean isBusted() {
            return isBusted;
        }

        public void setBusted() {
            this.isBusted = true;
            this.isFinished = true;
        }

        public int getFinalBet() {
            return finalBet;
        }

        public void setFinalBet(int finalBet) {
            this.finalBet = finalBet;
        }

        public boolean hasAce() {
            for (int i = 0; i < this.size(); i++) {
                if (this.getValue(i) == 11) {
                    return true;
                }
            }
            return false;
        }

        public void lockChoice() {
            choiceLocked = true;
        }

        public boolean choiceLocked() {
            return choiceLocked;
        }

}

