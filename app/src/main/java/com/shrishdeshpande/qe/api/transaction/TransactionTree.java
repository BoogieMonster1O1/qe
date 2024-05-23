package com.shrishdeshpande.qe.api.transaction;

public class TransactionTree {
    public static class Node {
        private final Transaction transaction;
        private final String hash;
        private Node left;
        private Node right;

        public Node() {
            this.transaction = null;
        }

        public Node(Transaction transaction) {
            this.transaction = transaction;
            this.hash = transaction.hash();
        }

        public Transaction getTransaction() {
            return transaction;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }
}
