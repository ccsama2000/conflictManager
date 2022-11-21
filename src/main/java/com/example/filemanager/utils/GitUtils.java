package com.example.filemanager.utils;


import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GitUtils {

    public Repository getRepository(String path) throws Exception{
        File gitDirectory=new File(path);
        Repository repository;
        FileRepositoryBuilder builder=new FileRepositoryBuilder();
        repository=builder.setGitDir(new File(gitDirectory,".git"))
                    .readEnvironment()
                    .findGitDir()
                    .build();
        return repository;
    }

    public List<RevCommit> getMergeCommits(Repository repository) throws Exception{
        List<RevCommit> commits=new ArrayList<>();
        try (RevWalk revWalk = new RevWalk(repository)) {
            for (Ref ref : repository.getRefDatabase().getRefs()) {
                revWalk.markStart(revWalk.parseCommit(ref.getObjectId()));
            }
            for (RevCommit commit : revWalk) {
                if (commit.getParentCount() == 2) {
                    commits.add(commit);
                }
            }
        }
        return commits;
    }

    public RevCommit getSpecificCommits(Repository repository, ObjectId id) throws Exception{
        try (RevWalk revWalk = new RevWalk(repository)) {
            for (Ref ref : repository.getRefDatabase().getRefs()) {
                revWalk.markStart(revWalk.parseCommit(ref.getObjectId()));
            }
            for (RevCommit commit : revWalk) {
                if (commit.getId().equals(id)) {
                    return commit;
                }
            }
        }
        return null;
    }
}
