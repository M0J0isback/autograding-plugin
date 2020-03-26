package io.jenkins.plugins.grading;

import edu.hm.hafner.util.VisibleForTesting;

import org.kohsuke.stapler.StaplerProxy;
import hudson.model.Run;

import io.jenkins.plugins.util.BuildAction;

/**
 * Controls the live cycle of the score results in a job. This action persists the results of a build and displays a
 * summary on the build page. The actual visualization of the results is defined in the matching {@code summary.jelly}
 * file.
 *
 * @author Eva-Maria Zeintl
 */
public class AutoGradingBuildAction extends BuildAction<Score> implements StaplerProxy {
    private static final long serialVersionUID = -1165416468486465651L;

    /**
     * Creates a new instance of {@link AutoGradingBuildAction}.
     *
     * @param owner
     *         the associated build that created the scores
     * @param score
     *         score instance where all results are saved
     */
    public AutoGradingBuildAction(final Run<?, ?> owner, final Score score) {
        this(owner, score, true);
    }

    @VisibleForTesting
    AutoGradingBuildAction(final Run<?, ?> owner, final Score score, final boolean canSerialize) {
        super(owner, score, canSerialize);
    }

    @Override
    protected ScoreXmlStream createXmlStream() {
        return new ScoreXmlStream();
    }

    @Override
    protected AutoGradingJobAction createProjectAction() {
        return new AutoGradingJobAction(getOwner().getParent());
    }

    @Override
    protected String getBuildResultBaseName() {
        return "auto-grading.xml";
    }

    @Override
    public String getIconFileName() {
        return AutoGradingJobAction.SMALL_ICON;
    }

    @Override
    public String getDisplayName() {
        return Messages.Action_Name();
    }

    /**
     * Returns the detail view for the autograding data for all Stapler requests.
     *
     * @return the detail view for the autograding data
     */
    @Override
    public Object getTarget() {
        return new AutoGradingViewModel(getOwner(), getResult());
    }

    @Override
    public String getUrlName() {
        return AutoGradingJobAction.ID;
    }

    @SuppressWarnings("unused")
    public String getTotalScore() {
        return getResult().getScore() + "/" + getResult().getConfigs().getMaxScore();
    }
}