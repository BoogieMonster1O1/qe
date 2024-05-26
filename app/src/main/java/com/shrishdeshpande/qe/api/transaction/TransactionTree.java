package com.shrishdeshpande.qe.api.transaction;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TransactionTree {
    private final Node root;

    public TransactionTree(List<Transaction> list) {
        list.sort(Comparator.comparing(Transaction::getTimestamp));

        List<Node> leaves = createLeafNodes(list);

        if (leaves.isEmpty()) {
            root = new Node();
        } else {
            root = buildMerkleTree(leaves);
        }
    }

    public String rootHash() {
        return root.getHash();
    }

    private List<Node> createLeafNodes(List<Transaction> transactions) {
        return transactions.stream()
                .map(Node::new)
                .toList();
    }

    private Node buildMerkleTree(List<Node> nodes) {
        while (nodes.size() > 1) {
            List<Node> newLevel = new ArrayList<>();

            for (int i = 0; i < nodes.size(); i += 2) {
                Node left = nodes.get(i);
                Node right = (i + 1 < nodes.size()) ? nodes.get(i + 1) : new Node(); // Handle odd number of nodes

                Node parent = new Node();
                parent.setLeft(left);
                parent.setRight(right);
                newLevel.add(parent);
            }

            nodes = newLevel;
        }

        return nodes.getFirst();
    }

    public static class Node {
        private Transaction transaction;
        private String hash;
        private Node left;
        private Node right;

        public Node() {
            this.transaction = null;
            this.hash = null;
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

        public String getHash() {
            if (transaction != null) {
                return hash;
            } else if (left == null || right == null) {
                return "";
            }

            return DigestUtils.sha256Hex(left.getHash() + right.getHash());
        }
    }
}
